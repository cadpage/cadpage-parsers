package net.anei.cadpage.parsers.NJ;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class NJBurlingtonCountyHParser extends DispatchH05Parser {

  public NJBurlingtonCountyHParser() {
    super(CITY_CODES, "BURLINGTON COUNTY", "NJ",
          "FINAL? ( RADIO_CHANNEL:CH | Radio_Channel:CH | ) " +
              "( Call_Type:SKIP! Date:DATETIME! Inc_Number:ID! Common_Name:PLACE! Address:ADDRCITY! Additional_Location_Info:INFO! Cross_Streets:X! " +
                "Caller_Name:NAME! Address:SKIP! Phone:PHONE! NATURE_OF_CALL:CALL! NARRATIVE:EMPTY! INFO_BLK+? UNIT_TIMES:EMPTY! TIMES+ Alerts:ALERT! " +
              "| ( TYPE:CALL! | INC_TYPE:CALL! ) DATE:DATETIME! INC_NUMBER:ID! COMMON_NAME:PLACE! ( ADDRESS:ADDRCITY! | INC_ADDRESS:ADDRCITY! ) ( \"LOCAL_INFO\":PLACE! | DETAILED_LOCATION:PLACE! | DETAILED_INFO:PLACE! ) " +
                "CROSS_STREETS:X! ( NAME:NAME! | CALLERS_NAME:NAME! ) ADDRESS:SKIP? PHONE:PHONE! ALERTS:ALERT? NATURE_OF_CALL:CALL/SDS? ( NARRATIVE:EMPTY! INFO_BLK+? | ) UNITS_DISPATCHED:UNIT? " +
                "UNIT_TIMES:EMPTY? TIMES+? ( FIRE_GRID:MAP! | ALERTS:ALERT! FINAL_REPRT:GPS2 | NATURE:EMPTY ALERTS:ALERT! FINAL_REPRT:GPS2 | UNITS_DISPATCHED:UNIT! EMS_GRID:MAP! EMPTY+? GPS | END ) https:QUERY " +
              "| CALL! RADIO_CHANNEL:CH! INC_NUMBER:EMPTY! ID! COMMON_NAME:EMPTY! NAME CALL_ADDRESS:EMPTY! ADDRCITY! QUALIFIER/LOCAL_INFO:EMPTY! INFO/N+ CROSS_STREETS:EMPTY! X " +
                "CALLERS_NAME:NAME! CALLERS_ADDRESS:SKIP! CALLERS_PHONE:PHONE! UNITS_ASSIGNED:EMPTY! UNIT ALERTS:EMPTY! ALERT INFO/N+ END " +
              "| CALL! CALL2+ INC_NUMBER:EMPTY! ID! ADDRCITY! CROSS_STREETS:EMPTY! X NAME:NAME! ADDRESS:SKIP! PHONE:PHONE! UNITS_ASSIGNED:EMPTY! UNIT! ALERTS:EMPTY! ALERT INFO/N+ " +
              ")");
    setAccumulateUnits(true);
    setupMultiWordStreets("REV DR ML KING JR");
  }

  @Override
  public String getFilter() {
    return "@co.burlington.nj.us,@CinnaminsonPolice.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern SPEC_DELIM = Pattern.compile("(?:=20)*\n+|<br>|(?<=\\b\\d\\d:\\d\\d:\\d\\d) (?=[A-Z0-9]+\\\\)");

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    if (body.startsWith("CAUTION:")) {
      int pt = body.indexOf('\n', 8);
      if (pt < 0) return false;
      body = body.substring(pt).trim();
      return parseFields(body.split("\n"), data);
    }
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();

    if (subject.equals("Station 261")) {
      data.strSource = subject;
      return parseFields(SPEC_DELIM.split(body), data);
    }

    if (subject.equals("!")) {
      return parseFields(body.split("\n"), data);
    }

    if (subject.startsWith("[")) {
      pt = subject.indexOf(']');
      if (pt < 0) return false;
      subject = subject.substring(pt+1).trim();
    }

    pt = body.indexOf("\nThe information in this e-mail");
    if (pt >= 0) body = body.substring(0,pt);
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public String getProgram() {
    return "SRC? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("FINAL")) return new SkipField("Final", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("MAP"))  return new MyMapField();
    if (name.equals("QUERY")) return new MyQueryField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("GPS2")) return new MyGPS2Field();
    return super.getField(name);
  }


  private static final Pattern CITY_PTN = Pattern.compile("(\\d\\d) *(.*)");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM|SUITE|UNIT) *(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String city = null;
      int pt = field.indexOf(',');
      if (pt >= 0) {
        city = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      field = field.replace('@', '&');
      int flags = FLAG_RECHECK_APT | FLAG_ANCHOR_END;
      if (city != null) flags |= FLAG_NO_CITY;
      parseAddress(StartType.START_ADDR, flags, field, data);

      if (city != null) {
        city = stripFieldStart(city, data.strApt);
        Matcher match = CITY_PTN.matcher(city);
        if (match.matches()) {
          data.strCity = convertCodes(match.group(1), CITY_CODES);
          city = match.group(2);
//          if (NUMERIC.matcher(data.strCity).matches()) abort();
        }
        if (city.length() > 0) {
          match = APT_PTN.matcher(city);
          if (match.matches()) city = match.group(1);
          if (!city.equals(data.strApt)) data.strApt = append(data.strApt, "-", city);
        }
      }
    }
  }

  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      super.parse(field, data);
    }
  }

  private static final Pattern MAP_PTN = Pattern.compile("(.*?) *(EMS|FIRE) GRID: *(.*)");
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = MAP_PTN.matcher(field);
      if (match.matches()) {
        String map1 = match.group(1);
        String type2 = match.group(2).substring(0,1);
        String map2 = match.group(2);
        if (map1.equals(map2)) {
          field = map1;
        } else {
          String type1 = (type2.equals("E") ? "F" : "E");
          if (!map1.isEmpty()) map1 = type1 + ':' + map1;
          if (!map2.isEmpty()) map2 = type2 + ':' + map2;
          field = append(map1, " ", map2);
        }
      }
      super.parse(field, data);
    }
  }

  private static final Pattern QUERY_PTN = Pattern.compile("//www.google.com/maps/.*?(?:,([A-Z+]{3,}))?(?:,([A-Z]{2}))?");
  private class MyQueryField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = QUERY_PTN.matcher(field);
      if (match.matches()) {
        if (data.strCity.length() == 0 || NUMERIC.matcher(data.strCity).matches()) {
          String city = match.group(1);
          if (city != null) {
            city = city.trim().replace('+', ' ');
            data.strCity = stripFieldEnd(city, " BORO");
          }
        }
        data.strState = getOptGroup(match.group(2));
      }
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }

  private class MyCall2Field extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (field.equals(getRelativeField(-1))) return;
      super.parse(field,  data);
    }
  }

  private class MyGPS2Field extends GPSField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("LAT/LONG:");
      if (pt < 0) return;
      field = field.substring(pt+9).replace("-", ",-");
      super.parse(field, data);
    }
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.startsWith("EXIT")) return true;
    return super.isNotExtraApt(apt);
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = CL_PTN.matcher(addr).replaceAll("CIR");
    addr = TN_PTN.matcher(addr).replaceAll("TURN");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern CL_PTN = Pattern.compile("\\bCL\\b");
  private static final Pattern TN_PTN = Pattern.compile("\\bTN\\b", Pattern.CASE_INSENSITIVE);

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "04", "FLORENCE TWP",
      "10", "MAPLE SHADE TWP",
      "11", "DELANCO TWP",
      "12", "BEVERLY",
      "13", "LUMBERTON TWP",
      "14", "EDGEWATER PARK TWP",
      "16", "WILLINGBORO TWP",
      "17", "SOUTHAMPTON TWP",
      "18", "PEMBERTON TWP",
      "19", "PEMBERTON",
      "20", "CINNAMINSON TWP",
      "21", "COLUMBUS",
      "22", "EVESHAM TWP",
      "23", "DELRAN TWP",
      "24", "RIVERTON",
      "25", "MEDFORD TWP",
      "26", "CHESTERFIELD TWP",
      "27", "WESTAMPTON TWP",
      "28", "SHAMONG TWP",
      "29", "WOODLAND TWP",
      "30", "BURLINGTON TWP",
      "31", "MOORESTOWN TWP",
      "32", "BORDENTOWN TWP",
      "33", "MANSFIELD TWP",
      "34", "EASTAMPTON TWP",
      "36", "MOUNT LAUREL TWP",
      "37", "MEDFORD LAKES",
      "38", "COOKSTOWN",
      "39", "HAINESPORT TWP",
      "40", "ROEBLING",
      "41", "WRIGHTSTOWN",
      "42", "TUCKERTON",
      "43", "TABERNACLE TWP",
      "44", "FIELDSBORO",
      "45", "EGG HARBOR CITY",
      "46", "WRIGHTSTOWN",
      "50", "MOUNT HOLLY TWP",
      "60", "BORDENTOWN",
      "61", "MILLICA TWP",
      "62", "PENNSAUKEN TWP",
      "63", "NEW EGYPT",
      "64", "ALLENTOWN",
      "65", "TUCKERTON",
      "70", "RIVERSIDE TWP",
      "80", "PALMYRA",
      "90", "BURLINGTON"

  });

}
