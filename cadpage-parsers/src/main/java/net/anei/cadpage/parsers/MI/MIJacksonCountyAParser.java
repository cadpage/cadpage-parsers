package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

/**
 * Jackson County, MI
 */
public class MIJacksonCountyAParser extends DispatchH05Parser {

  public MIJacksonCountyAParser() {
    super("JACKSON COUNTY", "MI",
          "( Call_Address:ADDRCITY! Call_Date/Time:DATETIME! Narrative:EMPTY! INFO_BLK+ Fire_Call_Type:CALL! " +
          "| Narrative:EMPTY! INFO_BLK+ Call_Address:ADDRCITY! Call_Date/Time:DATETIME! Fire_Call_Type:CALL! " +
          ") Fire_Quadrant:MAP Incident_Number:ID! Units_Assigned:UNIT! Status_Times:EMPTY! TIMES+ Google_Maps_Hyperlink:EMPTY! GPS! Units_Assigned:SKIP END ");
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
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("GPS")) return new GPSField("https://.*query=(.*)", true);
    return super.getField(name);
  }
}
