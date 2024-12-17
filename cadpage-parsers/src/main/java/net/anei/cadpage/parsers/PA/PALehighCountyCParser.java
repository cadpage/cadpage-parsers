package net.anei.cadpage.parsers.PA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PALehighCountyCParser extends FieldProgramParser {

  public PALehighCountyCParser() {
    super(CITY_LIST, "LEHIGH COUNTY", "PA",
          "CALL! Address:ADDRCITY! XSt:X! " +
              "( PHONE/Z Caller:NAME_PHONE! INFO/N+ Assigned_Units:UNIT! Radio_Channel:CH! GPS1! GPS2! Fire_Response_Area:MAP? EMS_Response_Area:MAP/L? " +
              "| INFO/N+ Assigned_Units:UNIT! ( GPS! | GPS1 GPS2 EMPTY! ) ) END");
  }

  @Override
  public String getFilter() {
    return "dispatch@lehighcounty.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern TRAIL_DATE_TIME_PTN = Pattern.compile(" +(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)(?: \\| Alarm Level: *(\\d))?$");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private static final Pattern DELIM = Pattern.compile("\\s*\\|\\s+");

  @Override
  protected boolean parseMsg(String body, Data data) {
//    body = body.replace('\n', ' ');
    Matcher match = TRAIL_DATE_TIME_PTN.matcher(body);
    if (!match.find()) return false;
    body = body.substring(0,match.start());
    data.strDate = match.group(1);
    String time = match.group(2);
    if (time.endsWith("M")) {
      setTime(TIME_FMT, time, data);
    } else {
      data.strTime = time;
    }
    data.strPriority = getOptGroup(match.group(3));
    return parseFields(DELIM.split(body, -1), data);
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " DATE TIME PRI";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("NAME_PHONE")) return new MyNamePhoneField();
    if (name.equals("GPS")) return new GPSField("-361 -361|[-+]?\\d{2}\\.\\d{6,} [-+]?\\d{2}\\.\\d{6,}", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String city = null;
      int pt = field.indexOf(',');
      if (pt >= 0) {
        city = field.substring(pt+1).trim();
        field = field.substring(0, pt).trim();
      }
      field = field.replace('@',  '&');
      parseAddress(field, data);

      if (city != null) {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
        data.strPlace = getLeft();
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY PLACE";
    }
  }

  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      super.parse(field, data);
    }
  }

  private static final Pattern NAME_PHONE_PTN = Pattern.compile("(.*?) +(\\d{10})");
  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");

  private class MyNamePhoneField extends NameField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = NAME_PHONE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strPhone = match.group(2);
      }
      super.parse(MSPACE_PTN.matcher(field).replaceAll(" "), data);
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }
  }

  private static final String[] CITY_LIST = new String[]{

        // Cities
        "ALLENTOWN",
        "BETHLEHEM",

        // Boroughs
        "ALBURTIS",
        "CATASAUQUA",
        "COOPERSBURG",
        "COPLAY",
        "EMMAUS",
        "FOUNTAIN HILL",
        "MACUNGIE",
        "SLATINGTON",

        // Townships
        "HANOVER",
        "HEIDELBERG",
        "LOWER MACUNGIE",
        "LOWER MILFORD",
        "LOWHILL",
        "LYNN",
        "NORTH WHITEHALL",
        "SALISBURY",
        "SOUTH WHITEHALL",
        "UPPER MACUNGIE",
        "UPPER MILFORD",
        "UPPER SAUCON",
        "WASHINGTON",
        "WEISENBERG",
        "WHITEHALL",

        // Census-designated places
        "ANCIENT OAKS",
        "BREINIGSVILLE",
        "CEMENTON",
        "CETRONIA",
        "DESALES UNIVERSITY",
        "DORNEYVILLE",
        "EGYPT",
        "FULLERTON",
        "HOKENDAUQUA",
        "LAURYS STATION",
        "NEW TRIPOLI",
        "SCHNECKSVILLE",
        "SLATEDALE",
        "STILES",
        "TREXLERTOWN",
        "WESCOSVILLE",

        // Unincorporated communities
        "BALLIETTSVILLE",
        "CENTER VALLEY",
        "COLESVILLE",
        "EAST TEXAS",
        "EMERALD",
        "EVERGREEN PARK",
        "FOGELSVILLE",
        "GAUFF HILL",
        "GERMANSVILLE",
        "GUTHSVILLE",
        "HENSINGERSVILLE",
        "HOSENSACK",
        "IRONTON",
        "KUHNSVILLE",
        "LANARK",
        "LIMEPORT",
        "LOCUST VALLEY",
        "LYNNPORT",
        "MECHANICSVILLE",
        "MEYERSVILLE",
        "NEFFS",
        "NEW SMITHVILLE",
        "OLD ZIONSVILLE",
        "OREFIELD",
        "PLEASANT CORNERS",
        "POWDER VALLEY",
        "SCHERERSVILLE",
        "SCHOENERSVILLE",
        "SHIMERVILLE",
        "SUMMIT LAWN",
        "VERA CRUZ",
        "WALBERT",
        "WANAMAKERS",
        "WERLEYS CORNER",
        "WEST CATASAUQUA",
        "ZIONSVILLE",

        // Northampton County
        "WALNUTPORT"
  };
}
