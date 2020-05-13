package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class OKYukonParser extends DispatchH05Parser {
  public OKYukonParser() {
    super("YUKON", "OK", 
          "( CALL:CALL! PLACE:PLACE! ( ADDRESS:ADDRCITY/S6 | ADDR:ADDRCITY/S6! ) CROSS_ST:X! ( INCIDENT_#:ID! | INCIDENT_NUMBER:ID! | ID:ID! ) PRI:PRI? LAT/LON:GPS! INFO:INFO/N+ " + 
          "| Rip_and_Run_Report%EMPTY! CFS_Number:ID! Report_Date/Time:SKIP! Call_Date/Time:SKIP! Call_Address:ADDRCITY/S6! Police_Dispatchers:SKIP! Common_Name:PLACE! " +
              "Qualifier:LINFO! Google_Maps_Hyperlink:SKIP! Cross_Streets:X! Latitude:GPS1! Longitude:GPS2! Local_Information:LINFO! Custom_Layer:LINFO! Census_Tract:LINFO! " + 
              "EMS_Dispatchers:SKIP! EMS_District:MAP/L! EMS_Call_Type_Name:CODE! EMS_Call_Type_Description:CALL! EMS_Call_Priority:PRI! Nature_Of_Call:CALL/L! Units_Assigned:UNIT! " + 
              "Fire_Dispatchers:SKIP! Fire_Quadrant:MAP/L! Fire_Radio_Channel:CH/L! Fire_Call_Type_Name:CODE/L! Fire_Call_Type_Description:CALL/L! Fire_Call_Priority:PRI/L! " + 
              "Caller_Name:NAME! Caller_Phone:PHONE! Caller_Address:SKIP! Narratives:EMPTY! INFO_BLK+ Alerts:ALERT! ALERT/N+ Status_Times:EMPTY! TIMES Dispatch_Order:SKIP! " + 
          ")");
  }

  @Override
  public String getFilter() {
    return "@yukonok.gov";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("[Incident")) return;
      super.parse(field, data);
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\*{3}\\d\\d?/\\d\\d?/\\d{4}\\*{3}|\\d\\d?:\\d\\d:\\d\\d");
  private class MyInfoField extends InfoField {
    
    boolean skip = false;
    
    @Override
    public void parse(String field, Data data) {
      if (skip) {
        if (field.equals("-")) skip = false;
      } else {
        if (DATE_TIME_PTN.matcher(field).matches()) {
          skip = true;
        } else {
          data.strSupp = append(data.strSupp, "/n", field);
        }
      }
    }
  }
}
