package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA35Parser extends FieldProgramParser {
  
  public DispatchA35Parser(String defCity, String defState) {
    super(defCity, defState,
           "CALL! Loc:ADDR! Rcvd:TIME! Comments:INFO");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.startsWith("Times - ")) {
      data.strCall = "RUN REPORT";
      data.strPlace = '[' + subject + "] " + body;
      return true;
    }
    if (!subject.startsWith("Alarm - ")) return false;
    return parseMsg(body, data);
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return super.parseMsg(body, data);
  }
  
  private static final Pattern CALL_SRC_PTN = Pattern.compile("(.*?)[ ,]+([^ ]+)");
  private static final Pattern TRAIL_JUNK_PTN = Pattern.compile(" *-[^\\p{Graph}]*$");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      
      if (field.endsWith(",")) field = field.substring(0,field.length()-1).trim();
      
      Matcher match = CALL_SRC_PTN.matcher(field);
      if (!match.matches()) abort();
      field = match.group(1);
      data.strSource = match.group(2);
      
      match = TRAIL_JUNK_PTN.matcher(field);
      if (match.find()) field = field.substring(0,match.start());
      data.strCall = field;
    }
    
    @Override
    public String getFieldNames() {
      return "CALL SRC";
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strMap = p.getLastOptional("[ :");
      data.strCity = p.getLastOptional('[');
      if (data.strCity.length() == 0) data.strCity = p.getLastOptional(':');
      if (data.strCity.startsWith("@")) data.strCity = data.strCity.substring(1).trim();
      data.strPlace = p.getLastOptional('@');
      String addr = p.get();
      if (addr.endsWith("[")) addr = addr.substring(0,addr.length()-1).trim();
      parseAddress(addr, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE CITY MAP";
    }
  }
  
  private class MyTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt < 0) abort();
      super.parse(field.substring(0,pt), data);
      data.strCallId = field.substring(pt+1).trim();
    }
    
    @Override
    public String getFieldNames() {
      return "TIME ID";
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\s*\\b\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d?:\\d\\d [AP]M\\b *");
  private class MyInfoField extends InfoField {
    @Override 
    public void parse(String field, Data data) {
      field = DATE_TIME_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = field;
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

}
