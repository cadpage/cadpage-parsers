package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchH04Parser extends DispatchH05Parser {
  
  public DispatchH04Parser(String defCity, String defState) {
    super(defCity, defState, 
          "SKIP+? ( ADDR:ADDRCITY! PLACE:PLACE! Fire_Call_Type:CALL! EMS_CALL_TYPE:CALL2! " + 
                 "| ( CALL:CALL! | Fire_Call_Type:CALL! EMS_CALL_TYPE:CALL2 | FIRE_CALL_TYPE:CALL! EMS_CALL_TYPE:CALL2! | EMS_CALL_TYPE:CALL2 ) " + 
                     "PLACE:PLACE! ADDR:ADDRCITY! " +
                 ") " +
             "Lat_/_Long:GPS? ( Cross_Streets:X | CROSS_STREETS:X | ) CALLER_NAME:NAME? " + 
             "( CALLER_PHONE:PHONE | Caller_Phone:PHONE | ) Lat_/_Long:GPS? ID:ID? " + 
             "( PRI:PRI! | FIRE_PRIORITY:PRI! EMS_PRIORITY:PRI2! | ) DATE:DATETIME! " + 
             "( MAP:MAP! | FIRE_QUADRANT:MAP! EMS_District:MAP2! ) " + 
             "( UNIT:UNIT! | ASSIGNED_UNIT(s):UNIT! ) " + 
             "( INFO:INFO_BLK! | NARRATIVE:INFO_BLK! ) INFO_BLK/N+? TIMES:TIMES? TIMES+ " + 
             "CALLER_NAME:NAME FIRE_BOX:BOX INCIDENT_NUMBER:ID");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new BaseCall2Field();
    if (name.equals("ADDRCITY")) return new BaseAddressCityField();
    if (name.equals("PRI2")) return new BasePriority2Field();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("MAP2")) return new BaseMap2Field();
    return super.getField(name);
  }
  
  private class BaseCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strCall)) return;
      data.strCall = append(data.strCall, " / ", field);
    }
  }
  
  private static final Pattern ADDR_CITY_ST_PTN = Pattern.compile("(.*?,.*), *(.*)");
  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(",")) return;
      Matcher match = ADDR_CITY_ST_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }
  
  private class BasePriority2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strPriority)) return;
      data.strPriority = append(data.strPriority, " / ", field);
    }
  }
  
  private class BaseMap2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strMap)) return;
      data.strMap = append(data.strMap, " / ", field);
    }
  }
}
