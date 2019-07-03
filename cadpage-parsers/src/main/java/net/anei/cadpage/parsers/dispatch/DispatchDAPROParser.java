package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchDAPROParser extends FieldProgramParser {
  
  private static final String PROGRAM_STR = "( SELECT/1 ADDR/S6CX! | ADDR/S6! ) CFS:ID? INFO:INFO? Run:ID? CROSS:X";

  public DispatchDAPROParser(String defCity, String defState) {
    super(defCity, defState, PROGRAM_STR);
  }
  
  public DispatchDAPROParser(Properties cityCodeTable, String defCity, String defState) {
    super(cityCodeTable, defCity, defState, PROGRAM_STR);
  }
  
  public DispatchDAPROParser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState, PROGRAM_STR);
  }
  
  private static final Pattern MARKER1 = Pattern.compile("(?:\\d+:)?MAILBOX:");
  private static final Pattern MARKER2 = Pattern.compile("(?:\\d+:)?([-A-Z0-9]+) (?:(\\d{0,2}:\\d\\d) )?");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.length() > 0) {
      Parser p = new Parser(subject);
      data.strUnit = p.getLast(' ');
      data.strSource = p.get();
    }
    
    Matcher match = MARKER1.matcher(body);
    boolean mark =  match.lookingAt();
    if (mark) {
      body = body.substring(match.end()).trim();
    }
    String alertBody = body;
    
    match = MARKER2.matcher(body);
    if (!match.lookingAt()) {
      if (!mark) return false;
      setFieldList("INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = alertBody;
      return true;
    }
    data.strBox = match.group(1);

    String time = getOptGroup(match.group(2));
    if (time.startsWith(":")) time = '0' + time;
    data.strTime = time;
    body = body.substring(match.end());
    
    // A leading blank marks a missingn call description
    if (body.startsWith(" ")) {
      setSelectValue("2");
      body = body.trim();
    } else {
      setSelectValue("1");
    }
    
    int pt = body.indexOf(" CFS# ");
    if (pt >= 0) {
      pt += 4;
      body = body.substring(0,pt) + ':' + body.substring(pt+1);
    } else if ((pt = body.indexOf(" - ")) >= 0) {
      body = body.substring(0,pt) + " INFO:" + body.substring(pt+3);
    } else if (mark) {
      setFieldList("INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = alertBody;
      data.strSource = "";
      return true;
    } else return false;
    
    body = body.replace(" Run# ", " Run: ");
    
    if (!super.parseMsg(body, data)) return false;
    return mark || data.strCallId.length() > 0;
  }
  
  @Override
  public String getProgram() {
    return "SRC UNIT BOX TIME " + super.getProgram();
  }
  
  @Override 
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new BaseIdField();
    if (name.equals("X")) return new BaseCrossField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_DISREGARD_PTN = Pattern.compile("[-A-Z0-9]+ DISREGARD PAGE \\d\\d?:\\d\\d +");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String cancel = null;
      Matcher match = ADDR_DISREGARD_PTN.matcher(field);
      if (match.lookingAt()) {
        cancel = match.group();
        field = field.substring(match.end());
      }
      super.parse(field, data);
      if (cancel != null) data.strCall = cancel + data.strCall;
    }
  }
  
  private static final Pattern ID_PTN = Pattern.compile("((?:\\d{4}-)?\\d{6}),? *(.*)");
  private class BaseIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCallId = match.group(1);
      data.strSupp = match.group(2);
    }
    
    @Override
    public String getFieldNames() {
      return "ID INFO";
    }
  }
  
  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      super.parse(field, data);
    }
  }
}
