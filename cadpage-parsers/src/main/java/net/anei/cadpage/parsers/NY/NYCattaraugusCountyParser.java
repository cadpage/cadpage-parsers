package net.anei.cadpage.parsers.NY;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;




public class NYCattaraugusCountyParser extends FieldProgramParser {

  private String address;

  public NYCattaraugusCountyParser() {
    super(CITY_CODES, "CATTARAUGUS COUNTY", "NY",
           "SRC Unit:UNIT? Loc:ADDRCITY/S6! Between:X! CN:PLACE CTV:CITY Type:CALL Date:DATE Time:TIME Info:INFO Caller:NAME Inc:ID%");
  }

  @Override
  public String getFilter() {
    return "911@cattco.org,messaging@iamresponding.com,777,888,0583";
  }

  private static Pattern MARKER = Pattern.compile("CATTARAUGUS COUNTY SHERIFF:? *");
  private static Pattern TRAIL_COMMA_PAT = Pattern.compile("[ ,]+$");
  private static Pattern LOCATION_PAT = Pattern.compile(".* COUNTY", Pattern.CASE_INSENSITIVE);
  private static final Pattern CALL_DATE_TIME_PTN = Pattern.compile("(.*) (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    Matcher match = MARKER.matcher(body);
    if (match.lookingAt()) {
      body = body.substring(match.end());
      if (body.startsWith("(")) {
        int parenCnt = 1;
        int pt = 1;
        for (; parenCnt > 0 && pt < body.length(); pt++) {
          char chr = body.charAt(pt);
          if (chr == '(') parenCnt++;
          else if (chr == ')') parenCnt--;
        }
        subject = body.substring(1,pt-1).trim();
        body = body.substring(pt).trim();
      }
    }

    // Silly IAR edits :(
    if (subject.equals("WVFD")) {
      if (!body.startsWith("Loc:")) {
        body = "Loc:" + body.replace("\u0000", "")
                             .replace("\n`\nBtw:", " Between:")
                             .replaceAll("\n`\n", " ");
      }
      body = subject + ' ' + body;
    }

    body = body.replace(" Inc#:", " Inc:");
    address = null;
    if (!super.parseMsg(body, data)) return false;

    // A city code of OUTS -> OUTSIDE the county means we know nothing
    // about the county or state where this incident occurs :(
    if (data.strCity.equals("OUTSIDE")) {
      data.strCity = data.defCity = data.defState = "";

      // See if info field contains the entered address.  If it does, assume
      // that whatever follows the address is really a city name
      int pt = data.strSupp.indexOf(address);
      if (pt > 0) {
        data.strCity = data.strSupp.substring(pt + address.length()).trim();
      }

      // Otherwise, see if the name field contains a county or city name
      else if (LOCATION_PAT.matcher(data.strName).matches()) {
        data.strCity = data.strName;
        data.strName = "";
      }
    }

    // See if call field contains date/time
    if (data.strTime.length() == 0) {
      match = CALL_DATE_TIME_PTN.matcher(data.strCall);
      if (match.matches()) {
        data.strCall = match.group(1).trim();
        data.strDate = match.group(2);
        setTime(TIME_FMT, match.group(3), data);
      }
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CALL DATE TIME");
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z0-9]{2,5}");
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATE")) return new MyDateTimeField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }

  private static final Pattern ADDR_PLACE_APT_PTN = Pattern.compile("(.*?) *\\bAPT\\b *(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      address = field;
      String apt = "";
      int pt = field.indexOf('*');
      if (pt >= 0) {
        String place = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        Matcher match = ADDR_PLACE_APT_PTN.matcher(place);
        if (match.matches()) {
          place = match.group(1);
          apt = match.group(2);
        }
        data.strPlace = place;
      }
      field = field.replace('@', '&');
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY PLACE APT";
    }
  }

  private static final Pattern CITY_PLACE_PTN = Pattern.compile("(.*[a-z]) +([A-Z][^a-z]+)");
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
      if (data.strCity.length() > 0) {
        Matcher match = CITY_PLACE_PTN.matcher(data.strCity);
        if (match.matches()) {
          data.strCity = match.group(1);
          data.strPlace = match.group(2);
        }
        data.strCity = stripFieldEnd(data.strCity, " Town");
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4})|(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match =  DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      if (data.strDate == null) {
        data.strDate = match.group(2);
        String time = match.group(3);
        if (time.endsWith("M")) {
          setTime(TIME_FMT, time, data);
        } else {
          data.strTime = time;
        }
      }
    }
  }

  // Name field needs to remove trailing commas
  private class MyNameField extends NameField {

    @Override
    public void parse(String field, Data data) {

      Matcher match = TRAIL_COMMA_PAT.matcher(field);
      if (match.find()) {
        field = field.substring(0, match.start());
      }
      super.parse(field, data);
    }
  }

  private static Properties CITY_CODES = buildCodeTable(new String[]{
     "ALLT","ALLEGANY",
     "ALLV","ALLEGANY",
     "ASHF","ASHFORD",
     "CARR","CARROLLTON",
     "CATT","CATTARAUGUS",
     "CLAR","CLARKSVILLE",
     "COLC","COLLINS CENTER",
     "COLD","COLDSPRING",
     "COLL","COLLINS",
     "CONE","CONEWANGO",
     "DAYT","DAYTON",
     "DELE","DELEVAN",
     "DELEVN","DELEVAN",
     "ELLI","ELLICOTTVILLE",
     "ELLT","ELLICOTTVILLE",
     "ELLV","ELLICOTTVILLE",
     "EOTT","EAST OTTO",
     "ERAN","EAST RANDOLPH",
     "FARM","FARMERSVILLE",
     "FRAT","FRANKLINVILLE",
     "FRAV","FRANKLINVILLE",
     "FREE","FREEDOM",
     "GENE","GENESEE",
     "GOWA","GOWANDA",
     "GVAL","GREAT VALLEY",
     "HINS","HINSDALE",
     "HUMP","HUMPHREY",
     "ISCH","ISCHUA",
     "LEON","LEON",
     "LIME","LIMESTONE",
     "LVLT","LITTLE VALLEY",
     "LVLV","LITTLE VALLEY",
     "LYND","LYNDON",
     "MACH","MACHIAS",
     "MANS","MANSFIELD",
     "NALB","NEW ALBION",
     "NAPO","NAPOLI",
     "OLEC","OLEAN CITY",
     "OLET","OLEAN",
     "OTTO","OTTO",
     "OUTS","OUTSIDE",
     "PERS","PERSIA",
     "PERT","PERRYSBURG",
     "PERV","PERRYSBURG",
     "PORT","PORTVILLE",
     "PORV","PORTVILLE",
     "RANT","RANDOLPH",
     "RANV","RANDOLPH",
     "REDH","RED HOUSE",
     "SALC","SALAMANCA CITY",
     "SALT","SALAMANCA",
     "SDAY","SOUTH DAYTON",
     "SPRGVLL","SPRINGVILLE",
     "SVAL","SOUTH VALLEY",
     "VILL","VILLENOVA",
     "YORK","YORKSHIRE"
  });
}
