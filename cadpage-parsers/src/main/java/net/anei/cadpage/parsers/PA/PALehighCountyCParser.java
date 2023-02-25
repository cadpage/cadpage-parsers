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
              "( PHONE/Z Caller:NAME! INFO/N+ Assigned_Units:UNIT! Radio_Channel:CH! GPS1! GPS2! Fire_Response_Area:MAP! EMS_Response_Area:MAP_DATE_TIME! " +
              "| INFO/N+ Assigned_Units:UNIT! ( GPS_DATE_TIME! | GPS1 GPS2 DATETIME! ) ) END");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@lehighcounty.org";
  }
  
  private static final Pattern DELIM = Pattern.compile(" ?\\| ");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('\n', ' ');
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("MAP_DATE_TIME")) return new MyMapDateTimeField();
    if (name.equals("GPS_DATE_TIME")) return new MyGPSDateTimeField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
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
  
  private static final Pattern GPS_DATE_TIME_PTN = Pattern.compile("(?:-361 -361|([-+]?\\d{2}\\.\\d{6,} [-+]?\\d{2}\\.\\d{6,})) +(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  
  private class MyGPSDateTimeField extends Field {
    
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = GPS_DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      String gps = match.group(1);
      if (gps != null) setGPSLoc(match.group(1), data);
      data.strDate = match.group(2);
      String time = match.group(3);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "GPS DATE TIME";
    }
  }
  
  private static final Pattern MAP_DATE_TIME_PTN = Pattern.compile("(.*?) *\\b(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private class MyMapDateTimeField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = MAP_DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strMap = append(data.strMap, "/", match.group(1));
      data.strDate = match.group(2);
      data.strTime = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "MAP DATE TIME";
    }
  }

  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher  match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
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
