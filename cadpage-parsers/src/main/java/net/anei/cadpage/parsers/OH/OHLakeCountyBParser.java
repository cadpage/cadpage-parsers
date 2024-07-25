package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class OHLakeCountyBParser extends DispatchH05Parser {

  public OHLakeCountyBParser() {
    super("LAKE COUNTY", "OH",
          "CALL_TYPE:CALL! NATURE_OF_CALL:CALL/SDS! CALL_DATE/TIME:DATETIMEID! ADDRESS:ADDRCITY! CROSS_STREETS:X! COMMON_NAME:PLACE! MAP! https:SKIP! " +
             "GPS! UNITS_ASSIGNED:UNIT! NARRATIVE:EMPTY! INFO_BLK+ ALERTS:ALERT! LOCAL_INFORMATION:LOC_INFO!");
  }

  @Override
  public String getFilter() {
    return "Fire_Calls@cityofmentor.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIMEID")) return new MyDateTimeIdField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("LOC_INFO")) return new MyLocalInfoField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_ID_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d:\\d\\d:\\d\\d) INCIDENT#:? *(.*?) CFS#:? *(.*)");
  private class MyDateTimeIdField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strCallId = append(match.group(3), "/", match.group(4));
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME ID";
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String apt = p.getLastOptional(" UNIT:");
      super.parse(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }

  private static final Pattern MAP_PTN = Pattern.compile("(\\S*) *QUAD: *(.*?) TOW:.*");
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      Matcher match =  MAP_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strMap = append(match.group(1), "/", match.group(2));
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("LAT/LONG:? *(.*)");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      super.parse(match.group(1).replace("-", ",-"), data);
    }
  }

  private Pattern APT_PTN = Pattern.compile("(?:ROOM|RM|SUITE|LOT|APT) *(.*)|(\\d{1,4}[A-Z]?|[A-Z])");
  private class MyLocalInfoField extends Field {

    @Override
    public void parse(String field, Data data) {
      field = field.toUpperCase();
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = match.group(2);
        data.strApt = append(data.strApt, "-", apt);
      } else {
        data.strPlace = append(field, " - ", data.strPlace);
      }

    }

    @Override
    public String getFieldNames() {
      // TODO Auto-generated method stub
      return null;
    }
  }
}
