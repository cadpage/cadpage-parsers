package net.anei.cadpage.parsers.MI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class MIOttawaCountyBParser extends FieldProgramParser {
  
  public MIOttawaCountyBParser() {
    super("OTTAWA COUNTY", "MI", 
          "Date/Time:DATETIME! Type:CALL_PRI! EMPTY! Call_Address:ADDRCITY! ( Cross_Streets:X! Common_Name:PLACE! | Common_Name:PLACE! Cross_Streets:X! ) Addtl_Location_Info:INFO? EMPTY! Nature:CALL! Narrative:INFO! INFO/N+? UNIT_TIMES+? Caller:NAME_PHONE");
  }
  
  @Override
  public String getFilter() {
    return "@occda.org";
  }
  
  private String times;
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification:")) return false;
    times = "";
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(times, "\n\n", data.strSupp);
    return true;
  }
  
  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT, true);
    if (name.equals("CALL_PRI")) return new PriorityField(".*[, ]+Pri: +(\\d+),?", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new InfoField("(?!Unit:).*");
    if (name.equals("UNIT_TIMES")) return new MyUnitTimesField();
    if (name.equals("NAME_PHONE")) return new MyNamePhoneField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*?)(?:, *(.*?))?(?: {3,}(\\S+))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY_PTN.matcher(field);
      if (!match.matches()) abort();   // Can't happen!
      String addr = match.group(1).trim();
      data.strCity = getOptGroup(match.group(2));
      String apt = match.group(3);
      if (apt != null) addr = stripFieldEnd(addr, ' ' + apt);
      parseAddress(addr, data);
      if (apt != null) {
        apt = stripFieldStart(apt, "#");
        data.strApt = append(data.strApt, "-", getOptGroup(apt));
      }
    }
  }
  
  private class MyUnitTimesField extends Field {
    
    private String unit = null;
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (field.startsWith("Unit:")) {
        unit = field.substring(5).trim();
        data.strUnit = append(data.strUnit, " ", unit);
      } else if (unit != null) {
        if (field.contains("Cleared at:")) data.msgType = MsgType.RUN_REPORT; 
        String timeInfo = "Unit: " + unit + '\n' + field.replace('\t', '\n');
        times = append(times, "\n\n", timeInfo);
        unit = null;
      }
    }
  
    @Override
    public String getFieldNames() {
      return "UNIT INFO";
    }
  }
  
  private class MyNamePhoneField extends Field {

    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf('(');
      if (pt >= 0) {
        data.strPhone = field.substring(pt);
        field = field.substring(0,pt);
      }
      field = stripFieldStart(field, ",");
      field = stripFieldEnd(field, ",");
      data.strName = field;
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }
  }
}
