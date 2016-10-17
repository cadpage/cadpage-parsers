package net.anei.cadpage.parsers.DE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Kent County, DE (F)
 */
public class DEKentCountyFParser extends DEKentCountyBaseParser {
  
  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
  
  public DEKentCountyFParser() {
    super("KENT COUNTY", "DE",
          "Call_Address:ADDRCITY! TAC_#:CH! Common_Name:PLACE! Qualifier:APT! Cross_Streets:X! Local_Information:INFO! Custom_Layer:PLACE! Census_Tract:EMPTY! Call_Type:CALL! Call_Priority:PRI! Call_Date/Time:DATETIME! Nature_Of_Call:INFO/N! Units_Assigned:UNIT! Fire_Quadrant:MAP! EMS_District:MAP! Incident_Number(s):ID! Caller_Name:NAME! Caller_Phone:PHONE! Caller_Address:SKIP! Alerts:SKIP!");
  }
  
  @Override
  public String getFilter() {
    return "NWSRipnRun@state.de.us";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.startsWith("Automatic R&R Notification: ")) return false;
    data.strCall = subject.substring(28).trim();
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT, true);
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {

      field = field.replace('@', '&');
      super.parse(field, data);
      adjustCityState(data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST PLACE";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
}


