package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.FieldProgramParser;

public class MIWashtenawCountyBParser extends FieldProgramParser {
  
  public MIWashtenawCountyBParser() {
    super("WASHTENAW COUNTY", "MI", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! ID:ID! PRI:PRI! DATE:DATETIME! INFO:INFO!");
    setFieldList("ID PRI CALL ADDR APT CITY");
  }
  
  @Override
  public String getFilter() {
    return "noreply@emergenthealth.org";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("(New Incident|Update to Incident|Incident Completed) - (\\d+)");
  private static final Pattern MASTER = Pattern.compile("New Incident:\n(.*?) - (.*?) at(?: (.*?)(?:, (.*?))?(?: (\\d{5}))?)?");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    String type = match.group(1);
    data.strCallId = match.group(2);
    
    if (body.startsWith("CALL:")) {
      body = body.replace("ID:", " ID:");
      return super.parseMsg(body, data);
    }
    
    match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strPriority = match.group(1);
    data.strCall = match.group(2);
    parseAddress(getOptGroup(match.group(3)), data);
    data.strCity = getOptGroup(match.group(4));
    if (data.strCity.length() == 0) data.strCity = getOptGroup(match.group(5));
    
    if (type.equals("Incident Completed")) {
      data.msgType = MsgType.RUN_REPORT;
    } else if (type.equals("Update to Incident")) {
      data.strCall = append("(UPDATE)", " ", data.strCall);
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*?) +(\\d{5})");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        zip = match.group(2);
      }
      super.parse(field, data);
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?-\\d\\d?-\\d{4}) (\\d\\d?:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1).replace('-', '/');
      data.strTime = match.group(2);
    }
  }
}
