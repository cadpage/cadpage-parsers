package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


public class DispatchA25Parser extends FieldProgramParser {
  
  private static final Pattern RUN_REPORT_ID_PTN = Pattern.compile(" INC #(\\d+-\\d+) ");
  private static final Pattern RUN_REPORT_ID_PTN2 = Pattern.compile("^Inc # (\\d+-\\d+)\\b");
  private static final Pattern RUN_REPORT_PTN2 = Pattern.compile("^OCC #\\d\\d-\\d+, INC #(\\d\\d-\\d+)");
  private static final Pattern MARKER = Pattern.compile("NEWOCC #OUTS  +|ALERT - OCC #OUTS +|NEW(?:INC|OCC) #([-0-9\\?]+) +");
  private static final Pattern MISSING_DELIM = Pattern.compile(",? (?=Phone:)");
  private static final Pattern ALTERNATE_PTN = Pattern.compile("NEW (?:(\\d\\d?-\\d\\d?-[A-Z]{1,2}) )?(.*?)(?:[-,] ([ A-Za-z]+))?");
  private static final Pattern PLACE_ADDR_PREFIX_PTN = Pattern.compile("([NSEW]B)|(.*)(?:&| and)", Pattern.CASE_INSENSITIVE);
  
  public DispatchA25Parser(String defCity, String defState) {
    super(defCity, defState,
           "CALL! CALL2+? Address:ADDR! Reporting_Person:NAME Phone:PHONE% Detail:INFO%");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.startsWith("HISTORY FOR ")) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      Matcher match = RUN_REPORT_ID_PTN.matcher(subject);
      if (match.find()) data.strCallId = match.group(1);
      data.strSupp = body;
      return true;
    }
    
    if (subject.startsWith("NEW 911 CALL HISTORY FOR UNIT #")) {
      setFieldList("UNIT ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strUnit = subject.substring(32).trim();
      Matcher match = RUN_REPORT_ID_PTN2.matcher(body);
      if (match.find()) data.strCallId = match.group(1);
      data.strSupp = body;
      return true;
      
    }
    
    Matcher match = RUN_REPORT_PTN2.matcher(body);
    if (match.find()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = body;
      return true;
    }
    
    match = MARKER.matcher(body);
    if (match.lookingAt()) {
      data.strCallId = getOptGroup(match.group(1));
      body = body.substring(match.end());
      body = MISSING_DELIM.matcher(body).replaceFirst("\n");
      return super.parseFields(body.split("\n"), data);
    }
    
    match = ALTERNATE_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("CODE CALL PLACE ADDR APT CITY");
      data.strCode = getOptGroup(match.group(1));
      String addr = match.group(2).trim();
      data.strCity = getOptGroup(match.group(3));
      
      String place = "";
      int pt = addr.lastIndexOf('@');
      if (pt >= 0) {
        place = addr.substring(pt+1).trim();
        addr = addr.substring(0,pt);
      }
      
      String apt = "";
      if (addr.endsWith(")")) {
        pt = addr.lastIndexOf('(');
        if (pt >= 0) {
          apt = addr.substring(pt+1, addr.length()-1).trim();
          addr = addr.substring(0,pt).trim();
          match = APT_PREFIX_PTN.matcher(apt);
          if (match.matches()) apt = match.group(1);
        }
      }
      
      pt = addr.indexOf(" - ");
      if (pt >= 0) {
        data.strCall = addr.substring(0,pt).trim();
        CodeSet callList = getCallList();
        if (callList != null) {
          String call = callList.getCode(data.strCall.toUpperCase(), true);
          if (call != null) {
            data.strPlace = data.strCall.substring(call.length()).trim();
            data.strCall = data.strCall.substring(0, call.length());
          }
        }
        parseAddress(addr.substring(pt+3).trim(), data);
      } else {
        parseAddress(StartType.START_CALL_PLACE, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, addr, data);
        if (data.strAddress.length() == 0) {
          if (data.strPlace.length() == 0) return false;
          parseAddress(data.strPlace, data);
          data.strPlace = "";
        }
      }
      
      data.strApt = append(data.strApt, "-", apt);
      
      // See if place looks like it should really be prefixed to the address
      match = PLACE_ADDR_PREFIX_PTN.matcher(data.strPlace);
      if (match.matches()) {
        String tmp = match.group(1);
        String sep = " ";
        if (tmp == null) {
          tmp = match.group(2).trim();
          sep = " & ";
        }
        data.strAddress = append(tmp, sep, data.strAddress);
        data.strPlace = "";
      }
      
      // Append previously identified place name
      data.strPlace = append(data.strPlace, " - ", place);
      return true;
    }
    return false;
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }
  
  private static final Pattern CALL_PTN = Pattern.compile("([-& A-Z0-9]+?) - (.*)", Pattern.CASE_INSENSITIVE);
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strCall = match.group(2).trim();
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private class MyCall2Field extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!data.strCall.endsWith("-")) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " ", field);
    }
  }
  
  private static final Pattern APT_PREFIX_PTN = Pattern.compile("(?:APT(?: ROOM)?|LOT|RM|ROOM|STE)[ :]*(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = p.getLastOptional(',');
      if (data.strCity.length() == 0) data.strCity = p.getLastOptional(" - ");
      data.strPlace = p.getOptional(" - ");
      String addr = p.get();
      String apt = "";
      if (addr.endsWith(")")) {
        int pt = addr.lastIndexOf('(');
        if (pt >= 0) {
          apt = addr.substring(pt+1, addr.length()-1).trim();
          addr = addr.substring(0,pt).trim();
          Matcher match = APT_PREFIX_PTN.matcher(apt);
          if (match.matches()) apt = match.group(1);
        }
      }
      parseAddress(addr, data);
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames() + " CITY";
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0 && ", Phone:".startsWith(field.substring(pt))) {
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
  }
}
