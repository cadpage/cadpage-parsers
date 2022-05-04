package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class PACambriaCountyParser extends FieldProgramParser {

  private static final Pattern MARKER1 = Pattern.compile("\\d\\d");
  private static final Pattern MARKER2 = Pattern.compile("^\\d\\d ");
  private static final Pattern BAR_PTN = Pattern.compile("([ \n])\\| ");

  public PACambriaCountyParser() {
    super(CITY_CODES, "CAMBRIA COUNTY", "PA",
           "( Date:DATE | ) ( Time:TIME! Nature:CALL! Add:ADDR/y! Cross:X? Fire_Area:MAP? UNIT | DATE:DATE! TIME CALL ADDR/y X UNIT! ) Sta:UNIT/CS? EMPTY+? GPS%");
  }

  @Override
  public String getFilter() {
    return "alerts@cambria.ealertgov.com,Cambria 9-1-1,messaging@iamresponding.com,90360";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (MARKER1.matcher(subject).matches()) {
      Matcher match = MARKER2.matcher(body);
      if (!match.find()) return false;
      body = body.substring(match.end()).trim();
    }

    body = body.replace("Location:", "Add:");
    String[] flds = body.split("\n *\\|?");
    if (flds.length < 5) flds = body.split("  ");
    if (flds.length >= 5) {
      if (!parseFields(flds, data)) return false;
    } else {
      body = BAR_PTN.matcher(body).replaceAll("$1Sta: ");
      if (body.endsWith("|")) body = body.substring(0,body.length()-1).trim();
      flds = body.split("\n");
      if (flds.length >= 5) {
        if (!parseFields(flds, data)) return false;
      } else {
        if (!super.parseMsg(body.replace('\n', ' '), data)) return false;
      }
    }
    data.strCity = stripFieldEnd(data.strCity, " Cambria County");
    data.strCity = stripFieldEnd(data.strCity, " Boro");
    data.strCity = stripFieldEnd(data.strCity, " BORO");
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new MyDateField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("TIME")) return new TimeField("(\\d\\d:\\d\\d:\\d\\d)(?:;.*)?", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS")) return new GPSField("https?://maps.google.com/\\?q=(.*)", true);
    return super.getField(name);
  }

  private static final Pattern DATE_PTN1 = Pattern.compile("\\d\\d/\\d\\d/\\d\\d(?:\\d\\d)?");
  private static final Pattern DATE_PTN2 = Pattern.compile("(\\d\\d)(\\d\\d)(\\d\\d)");
  private class MyDateField extends DateField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(';');
      if (pt >= 0) field = field.substring(0,pt).trim();
      if (DATE_PTN1.matcher(field).matches()) {
        super.parse(field, data);
      } else {
        Matcher match = DATE_PTN2.matcher(field);
        if (!match.matches()) abort();
        super.parse(match.group(1)+'/'+match.group(2)+'/'+match.group(3), data);
      }
    }
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Za-z0-9]+)-(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern NOT_CITY_PTN = Pattern.compile(".*EXIT.*");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      for (String city : CITY_LIST) {
        int pt = field.length() - city.length();
        if (pt < 0) continue;
        if (!city.equals(field.substring(pt).toUpperCase())) continue;
        if (pt > 0 && field.charAt(pt-1)!=' ') continue;
        data.strCity = field.substring(pt);
        field = field.substring(0,pt).trim();
        break;
      }

      super.parse(field, data);

      if (data.strCity.length() == 0 || data.strCity.endsWith(" COUNTY")) {
        int pt = data.strAddress.lastIndexOf("- ");
        if (pt >= 0) {
          String city = data.strAddress.substring(pt+2).trim();
          if (!NOT_CITY_PTN.matcher(city).matches()) {
            data.strCity = city;
            data.strAddress = data.strAddress.substring(0,pt).trim();
          }
        }
      }
    }
  }

  private static final Pattern CITY_CODE_PTN = Pattern.compile("-[A-Z]{2,4} ");
  private static final Pattern CITY_CODE_END_PTN = Pattern.compile("-[A-Z]{0,4}$");
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = CITY_CODE_PTN.matcher(field).replaceAll(" / ");
      field = CITY_CODE_END_PTN.matcher(field).replaceAll("");
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, field, data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ADAM", "ADAMS TWP",
      "AL",   "ALLEGHENY TWP",
      "ASHB", "ASHVILLE",
      "BARR", "BARR TWP",
      "BN",   "BROWNSTOWN",
      "BT",   "BLACKLICK TWP",
      "CA",   "CASSANDRA",
      "CB",   "CRESSON",
      "CF",   "CLEARFIELD TWP",
      "CH",   "CHEST TWP",
      "CL",   "CARROLLTOWN",
      "CM",   "CAMBRIA TWP",
      "CONT", "CONEMAUGH TWP",
      "CP",   "CHEST SPRINGS",
      "CR",   "CRESSON TWP",
      "CROY", "CROYLE TWP",
      "DB",   "DALE",
      "DE",   "DEAN TWP",
      "DT",   "DAISYTOWN",
      "EA",   "EAST TAYLOR TWP",
      "EB",   "EBENSBURG",
      "ECON", "EAST CONEMAUGH",
      "EF",   "EHRENFELD",
      "ECAR", "EAST CARROLL TWP",
      "ELDT", "ELDER TWP",
      "ET",   "ELDER TWP",
      "FB",   "FERNDALE",
      "FR",   "FANKLIN",
      "GB",   "GALLITZIN",
      "GE",   "GEISTOWN",
      "GT",   "GALLITZIN TWP",
      "HB",   "HASTINGS",
      "JO",   "JOHNSTOWN",
      "JT",   "JACKSON TWP",
      "LB",   "LORETTO",
      "LI",   "LILLY",
      "LO",   "LORAIN",
      "LY",   "LOWER YODER TWP",
      "MT",   "MIDDLE TAYLOR TWP",
      "MU",   "MUNSTER TWP",
      "NC",   "NORTHERN CAMBRIA",
      "NG",   "NANTY GLO",
      "PATB", "PATTON ",
      "PORB", "PORTAGE",
      "PORT", "PORTAGE TWP",
      "RI",   "RICHLAND TWP",
      "RT",   "READE TWP",
      "SB",   "SANKERTOWN",
      "SC",   "STONYCREEK TWP",
      "SUSQ", "SUSQUEHANNA TWP",
      "SF",   "SOUTH FORK",
      "SL",   "SCALP LEVEL",
      "SM",   "SOUTHMONT",
      "SQ",   "SUSQUEHANNA TWP",
      "SUMB", "SUMMERHILL",
      "SUMT", "SUMMERHILL TWP",
      "TB",   "TUNNELHILL",
      "UY",   "UPPER YODER TWP",
      "VB",   "VINTONDALE",
      "WASH", "WASHINGTON TWP",
      "WB",   "WESTOVER",
      "WC",   "WEST CARROLL TWP",
      "WE",   "WHITE TWP",
      "WI",   "WILMORE",
      "WILM", "WILMORE",
      "WM",   "WESTMONT",
      "WS",   "WEST TAYLOR TWP",
      "WT",   "WASHINGTON TWP",
      "WTAY", "WEST TAYLOR TWP",

      "CLF",  "CLEARFIELD COUNTY",
      "SOM",  "SOMERSET COUNTY"
  });

  private static final String[] CITY_LIST = new String[] {
    "CENTRAL CITY"
  };
}
