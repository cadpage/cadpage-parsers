package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class PAJuniataCountyCParser extends DispatchH05Parser {

  public PAJuniataCountyCParser() {
    super("JUNIATA COUNTY", "PA",
          "Rip_and_Run_Report%EMPTY CFS_Number:SKIP! Report_Date/Time:SKIP! Call_Address:ADDRCITY! Call_Taker:SKIP! Police_Dispatchers:SKIP! " +
          "Ready_for_Dispatch_Date/Time:SKIP! First_Unit_Dispatched_Date/Time:DATETIME! Incident_Numbers:ID! Common_Name:PLACE! Qualifier:APT! " +
          "Google_Maps_Hyperlink:EMPTY! Cross_Streets:X! Latitude:GPS1! Longitude:GPS2! Local_Information:EMPTY! Custom_Layer:LINFO! Census_Tract:LINFO " +
          "EMS_Dispatchers:SKIP! EMS_District:MAP! EMS_Radio_Channel:CH! EMS_Call_Type_Name:SKIP! EMS_Call_Type_Description:CALL! EMS_Call_Priority:PRI " +
          "Nature_Of_Call:CALL! Units_Assigned:UNIT! Fire_Dispatchers:SKIP! Fire_Quadrant:MAP! Fire_Radio_Channel:CH! Fire_Call_Type_Name:SKIP! " +
          "Fire_Call_Type_Description:CALL! Fire_Call_Priority:PRI! Caller_Name:NAME! Caller_Phone:PHONE! Caller_Address:SKIP! " +
          "Narratives:EMPTY! INFO_BLK+ Alerts:ALERT! Status_Times:EMPTY! TIMES+");
  }

  @Override
  public String getFilter() {
    return "noreply@jcpc911.com";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("PRI")) return new MyPriorityField();
    return super.getField(name);
  }

  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      data.strMap = merge(data.strMap, field);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = merge(data.strCall, field);
    }
  }

  private class MyPriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      data.strPriority = merge(data.strPriority, field);
    }
  }

  private String merge(String fld1, String fld2) {
    if (fld1.equals(fld2)) return fld1;
    return append(fld1, "/", fld2);
  }

}
