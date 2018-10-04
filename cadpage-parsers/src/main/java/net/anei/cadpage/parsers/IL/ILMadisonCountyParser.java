package net.anei.cadpage.parsers.IL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


/**
 * Madison County, IL
 */
public class ILMadisonCountyParser extends FieldProgramParser {
  
  public ILMadisonCountyParser() {
    super("MADISON COUNTY", "IL",
          "( Fire_Call_Type:CALL! Call_Address:ADDRCITY! Common_Name:PLACE! Cross_Streets:X! Nature_of_Call:CALL/SDS! Narrative:INFO! INFO/N+ Call_Date/Time:DATETIME! Caller_Name:NAME! Caller_Phone_#:PHONE! Status_Times:TIMES! Incident_Number:ID! Fire_Quadrant:SKIP! EMS_District:SKIP! Google_Map_Hyperlink:SKIP! " +
          "| ( CALL_RECEIVED_AT EMPTY | ) CALL EMPTY ( SELECT/1 PLACE EMPTY/Z ADDRCITY/S6 EMPTY ( ID EMPTY X! INFO/N+? TIMES+? " +
                                                                                               "| ( PHONE | X | APT ) EMPTY INFO/ZN+? ID EMPTY! TIMES+? INFO/N+ ) " + 
                                                    "| ADDRCITY/S6 EMPTY PLACE EMPTY X EMPTY APT EMPTY EMPTY+? INFO/ZN+? DATETIME EMPTY NAME EMPTY PHONE! TIMES+ " + 
                                                    ") " + 
          ")"); 
  }
  
  @Override
  public String getFilter() {
    return "@glen-carbon.il.us,@co.madison.il.us,@troypolice.us,@cityofedwardsville.com,@highlandil.gov";
  }

  private static final Pattern DELIM = Pattern.compile("[, ]*\n *");
  private static final Pattern FIND_ID_PTN = Pattern.compile("\\[(?:(\\d{4}-\\d{8})|Incident not yet created) ([A-Z]+\\d+)\\][ ,]*");
  
  String timeInfo;

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification:")) return false;

    String fmtCode = "1";
    
    int pt = body.indexOf("Fire Call Type:");
    if (pt >= 0) {
      body = body.substring(pt);
    }
    
    else {
      
      pt = subject.indexOf('|');
      if (pt >= 0) {
        String tmp = '[' + subject.substring(pt+1).trim() + ']';
        Matcher match = FIND_ID_PTN.matcher(tmp);
        if (match.matches()) {
          fmtCode = "2";
          String id = match.group(1);
          if (id != null) data.strCallId = id + ' ' + match.group(2);
          body = stripFieldStart(body, ",");
        }
      }
      
      while (true) {
        Matcher match = FIND_ID_PTN.matcher(body);
        if (!match.lookingAt()) {
          if (fmtCode.equals("1") && !match.find()) fmtCode = "2";
          break;
        }
        fmtCode = "2";
        String id = match.group(1);
        if (id != null) data.strCallId = append(data.strCallId, ", ", id + ' ' + match.group(2));
        body = body.substring(match.end());
      }
    }
    
    timeInfo = "";
    setSelectValue(fmtCode);
    if (!parseFields(DELIM.split(body), data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) {
      data.strSupp = append(timeInfo, "\n", data.strSupp);
    }
    
    // Remove duplicate apt
    if (data.strApt.length() > 0) {
      data.strAddress = stripFieldEnd(data.strAddress, ' ' + data.strApt);
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return "ID? " + super.getProgram();
  }
  
  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL_RECEIVED_AT")) return new SkipField("(?i)Call Rece?ie?ved at", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("URL")) return new SkipField("http://.*", true);
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT, true);
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("TIMES")) return new MyTimesField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_PHONE_PTN = Pattern.compile("(.*) (\\d{10})");
  private class MyAddressCityField extends AddressField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      return checkParse(field, data, false);
    }
    
    @Override
    public void parse(String field, Data data) {
      checkParse(field, data, true);
    }
    
    private boolean checkParse(String field, Data data, boolean force) {
      
      // If duplicate of place name, erase place name
      if (field.equals(data.strPlace)) data.strPlace = "";
      
      if (!field.equals("<UNKNOWN>")) {
        boolean good = false;
        int pt = field.lastIndexOf(',');
        if (pt >= 0) {
          good = true;
          data.strCity = field.substring(pt+1).trim();
          field = field.substring(0,pt).trim();
        }
        Matcher match = ADDR_PHONE_PTN.matcher(field);
        if (match.matches()) {
          good = true;
          field = match.group(1).trim();
          data.strPhone = match.group(2);
        }
        field = field.replace('@', '&');
        if (!force && !good && !isValidAddress(field, 1)) return false;
      }
      super.parse(field, data);
      return true;
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PHONE CITY";
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("<UNKNOWN>")) return;
      super.parse(field, data);
    }
  }
  
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strApt)) return;
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return true;
      if (field.contains("/") || isValidAddress(field)) {
        super.parse(field, data);
        return true;
      }
      return false;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private class MyIdField extends IdField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      String result = "";
      for (String part : field.split(", *")) {
        Matcher match = FIND_ID_PTN.matcher(part);
        if (!match.matches()) return false;
        String id = match.group(1);
        if (id != null) result = append(result, ", ", id + ' ' + match.group(2));
      }
      data.strCallId = append(data.strCallId, ", ", result);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      super.parse(field, data);
    }
  }
  
  private class MyPhoneField extends PhoneField {
    
    public MyPhoneField() {
      super("\\d{10}");
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern DISPATCH_TIME_PTN =  Pattern.compile("Dispatched at: (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)\\b.*");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyTimesField extends InfoField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("http:")) return false;
      if (INFO_MARK_PTN.matcher(field).lookingAt()) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (field.startsWith("Unit:")) {
        data.strUnit = append(data.strUnit, " ", field.substring(5).trim());
        timeInfo = append(timeInfo, "\n", field);
        return;
      }
      
      if (field.startsWith("http:")) return;
      
      Matcher match = DISPATCH_TIME_PTN.matcher(field);
      if (match.matches()) {
        data.strDate = match.group(1);
        setTime(TIME_FMT, match.group(2), data);
      }
      if (field.indexOf("Cleared at:") >= 0) data.msgType = MsgType.RUN_REPORT;
      field = "  " + field.replace("\t", "\n  ");
      timeInfo = append(timeInfo, "\n", field);
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT DATE TIME INFO";
    }
  }
  
  private static final Pattern INFO_END_PTN = Pattern.compile("[A-Z0-9]+: .*", Pattern.DOTALL);
  private static final Pattern INFO_MARK_PTN = Pattern.compile("\\*{3}\\d\\d?/\\d\\d?/\\d{4}\\*{3}");
  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *\\b\\d\\d:\\d\\d:\\d\\d [A-Za-z]+ - +");
  
  private class MyInfoField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (INFO_END_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
    
    
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("http:")) return;
      Matcher match = INFO_MARK_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end()).trim();
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
