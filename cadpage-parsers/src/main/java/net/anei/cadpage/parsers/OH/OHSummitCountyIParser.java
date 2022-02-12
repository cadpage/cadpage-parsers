package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class OHSummitCountyIParser extends DispatchH05Parser {

  public OHSummitCountyIParser() {
    super("SUMMIT COUNTY", "OH",
          "( Call_Time:DATETIME! Call_Type:CALL! Address:ADDRCITY! Common_Name:PLACE! Cross_Streets:X! Additional_Location_Info:INFO! Nature_of_Call:CALL! Assigned_Units:UNIT! Priority:PRI Quadrant:MAP! Incident_Number:ID! CFS_Number:SKIP Radio_Channel:CH! Narrative:EMPTY! INFO_BLK/N+ Times:EMPTY! TIMES+ " +
          "| Incident_Type:CALL! Incident_Number:ID! ( Call_Number:SKIP! | CFS_Number:SKIP! ) Incident_Location:ADDRCITY! Common_Name:PLACE! ( Located_between:X! | Cross_Streets:X! ) Fire_Quadrant:MAP! Call_Date_/_Time:SKIP! Dispatch:DATETIME! Caller:NAME! Nature_of_call:CALL/SDS! Narrative:INFO_BLK! INFO_BLK+ Hazards:ALERT! ( Lat/Lon:GPS! | GPS:GPS! ) Times:TIMES! TIMES+ Units_Assigned:UNIT! Radio_Channel:CH! END " +
          "| ID? ADDRCITY X/Z? GPS1 GPS2 DATETIME CALL INFO_BLK+? TIMES+? UNIT END )");
  }

  @Override
  public String getFilter() {
    return "noreply@sccad.summitoh.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern MISSING_BRK_PTN = Pattern.compile("(?=Call Type:|Common Name:|Additional Location Info:)");

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    // The did some weird editing that needs to be reversed
    body = MISSING_BRK_PTN.matcher(body).replaceAll("<div>");
    body = body.replace("Call time:", "Call Time:");

    return super.parseHtmlMsg(subject, body, data);
  }

  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\[.*\\]|", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}|-361");
  private class MyGPSField extends GPSField {

    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }
}
