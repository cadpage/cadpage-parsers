package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

/**
 * Jackson County, MI
 */
public class MIJacksonCountyAParser extends DispatchH05Parser {

  public MIJacksonCountyAParser() {
    super("JACKSON COUNTY", "MI",
          "( Call_Type:CALL! ( Call_Address:ADDRCITY! Call_Date/Time:DATETIME! Incident_Number:ID! Units_Assigned:UNIT! Nature_Of_Call:CALL! " +
                                 "cross_streets:X! Fire_Quadrant:MAP! google_maps_Hyperlink:EMPTY! GPS! Qualifier:EMPTY! Local_Information:INFO! INFO/N+ " +
                            "| Call_Date/Time:SKIP! Common_Name:PLACE! Call_Address:ADDRCITY! Call_Date/Time:SKIP! Narrative:EMPTY! INFO_BLK+ Apt/Lot:APT! " +
                                 "Cross_Streets:X! Incident_Number:ID! Goole_Maps_Hyperlink:EMPTY! GPS Alerts:ALERT! Caller:NAME! Status_Times:EMPTY! TIMES " +
                            "| Create_Date/Time:SKIP! Location:ADDRCITY! Narrative:EMPTY! INFO_BLK+ Common_Name:PLACE! Closest_Intersection:X! First_Unit:DATETIME Units_Assigned:UNIT " +
                            ") " +
          "| ( Call_Address:ADDRCITY! Call_Date/Time:DATETIME! Narrative:EMPTY! INFO_BLK+ Fire_Call_Type:CALL! " +
            "| Narrative:EMPTY! INFO_BLK+ Call_Address:ADDRCITY! Call_Date/Time:DATETIME! Fire_Call_Type:CALL! " +
            ") Fire_Quadrant:MAP Incident_Number:ID! Units_Assigned:UNIT! Status_Times:EMPTY! TIMES+ Google_Maps_Hyperlink:EMPTY! GPS! Units_Assigned:SKIP END " +
          ")");
  }

  @Override
  public String getFilter() {
    return "dispatcher@mijackson.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("GPS")) return new GPSField("https://.*query=(.*)", true);
    return super.getField(name);
  }

  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
}
