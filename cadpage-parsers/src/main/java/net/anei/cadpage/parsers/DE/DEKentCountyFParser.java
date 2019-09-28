package net.anei.cadpage.parsers.DE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Kent County, DE (F)
 */
public class DEKentCountyFParser extends DEKentCountyBaseParser {
  
  public DEKentCountyFParser() {
    super("KENT COUNTY", "DE",
          "( KC911_Rip_&_Run:ID! Date/Time:DATETIME! Call_Address:ADDRCITY! Common_Name:PLACE! Dev/Sub:PLACE! Cross_Streets:X! GPS! Type:CALL! Narrative:EMPTY! INFO/N+ " +
          "| Call_Address:ADDRCITY! TAC_#:CH! Common_Name:PLACE! Qualifier:APT! Cross_Streets:X! Local_Information:INFO! Custom_Layer:PLACE! Census_Tract:EMPTY! Call_Type:CALL! Call_Priority:PRI! Call_Date/Time:DATETIME! Nature_Of_Call:INFO/N! Units_Assigned:UNIT! Fire_Quadrant:MAP! EMS_District:MAP! Incident_Number(s):ID! Caller_Name:NAME! Caller_Phone:PHONE! Caller_Address:SKIP! Alerts:SKIP! )");
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
    return "CALL? " + super.getProgram();
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = field;
    }
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
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
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
}


