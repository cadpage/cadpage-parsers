package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class GAMeriwetherCountyParser extends FieldProgramParser {
  
  public GAMeriwetherCountyParser() {
    super(CITY_LIST, "MERIWETHER COUNTY", "GA", 
          "( SELECT/RR PLACE+? ADDR PLACE+ | CALL DATETIME PLACE+? ADDR! PLACE END )");
  }
  
  @Override
  public String getFilter() {
    return "MeriwetherGA@ez911mail.com";
  }
  
  private static final Pattern MASTER = Pattern.compile("(.*)\n(?: Sent (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d) From [A-Za-z ]+ ID|From : [a-z]+ User Id:) \\{\\d+\\}");
  private static final Pattern SUBJECT_PTN = Pattern.compile("(?:Disp|Clr): *(\\S+) *.*");
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("(\\d\\d?/\\d\\d/\\d{4}) (\\d\\d:\\d\\d(?::\\d\\d)?)(?:[- ]+(.*?))? - +(?:Case # (\\d+) )?/(.*)");
  private static final Pattern TIMES_BRK_PTN = Pattern.compile("(?: +/ *| +(?=ENR|ODO))");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    body = match.group(1).trim();
    data.strDate = getOptGroup(match.group(2));
    data.strTime = getOptGroup(match.group(3));
    
    // Real dispatch alerts always had a subject
    if (subject.length() > 0) {
      
      // See what we can get from the message subject
      match = SUBJECT_PTN.matcher(subject);
      if (match.matches()) {
        data.strUnit = match.group(1);
        subject = "";
      }
      
      // Try to parse a run report
      match = RUN_REPORT_PTN.matcher(body);
      if (match.matches()) {
        data.msgType = MsgType.RUN_REPORT;
        data.strDate = match.group(1);
        data.strTime = match.group(2);
        String addr = getOptGroup(match.group(3));   // Do something with the address
        data.strCallId = getOptGroup(match.group(4));
        data.strSupp = TIMES_BRK_PTN.matcher(match.group(5).trim()).replaceAll("\n");
        
        // Use the parse field engine to parse the address so we can reuse the main parsing logic
        if (addr.startsWith("- ")) addr = ' ' + addr;
        if (addr.endsWith(" -")) addr += ' ';
        setSelectValue("RR");
        parseFields(addr.split(" - "), data);
        
        // We have to do this after the call to parseFields
        setFieldList("DATE TIME PLACE ADDR CITY ID INFO");
        
        // If that didn't find anything and we have a unformated subject, use the subject as an address
        if (data.strAddress.length() == 0 && data.strPlace.length() == 0 && subject.length() > 0) {
          subject = stripFieldEnd(subject, " GA");
          int flags = FLAG_ANCHOR_END;
          if (data.strCity.length() > 0) flags |= FLAG_NO_CITY;
          parseAddress(StartType.START_ADDR, flags, subject, data);
        }
        return true;
      }
      
      // Try to parse as normal dispatch alert
      setSelectValue("");
      if (body.startsWith("- ")) body = ' ' + body;
      if (body.endsWith(" -")) body += ' ';
      if (parseFields(body.split(" - "), data))  return true;
      
      // If this fails, clean things up and report as general alert
      body = body.trim();
      String saveDate = data.strDate;
      String saveTime = data.strTime;
      data.initialize(this);
      data.strDate = saveDate;
      data.strTime = saveTime;
    }
    
    data.msgType = MsgType.GEN_ALERT;
    setFieldList("INFO");
    data.strSupp = body;
    return true;
  }
  
  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram() + " DATE TIME";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("Recv (\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d)", true);
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_PTN = Pattern.compile("(.*?) *\\bGA"); 
  private class MyAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      
      // Addresss always end with the GA state code
      Matcher match = ADDR_PTN.matcher(field);
      if (!match.matches()) return false;
      field = match.group(1);
      
      // What is left may consist of a naked city name
      if (isCity(field)) {
        data.strCity = field;
        field = "";
      }
      
      // If we are left with nothing, pull in the previous fields from the place
      if (field.length() == 0) {
        field = data.strPlace;
        data.strPlace = "";
      }
      
      // OK, parse what we have as an address
      field = field.replaceAll("  +", " ");
      field = field.replace('@', '&');
      int flags = FLAG_ANCHOR_END;
      if (data.strCity.length() > 0) flags |= FLAG_NO_CITY;
      parseAddress(StartType.START_ADDR, flags, field, data);
      
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY";
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
    "ALVATON",
    "GAY",
    "GREENVILLE",
    "LONE OAK",
    "LUTHERSVILLE",
    "MANCHESTER",
    "WARM SPRINGS",
    "WARMSPRINGS",
    "WOODBURY",
    
    // Troup County 
    "HOGANSVILLE"
  };
}
