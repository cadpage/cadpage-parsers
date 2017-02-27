package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class MIWashtenawCountyParser extends FieldProgramParser {

  public MIWashtenawCountyParser() {
    super("WASHTENAW COUNTY", "MI", 
          "( ADDR_CITY_ST | ADDR CITY_ST_ZIP ) EMPTY APT! PRI? INFO+ Cross:X! INFO+");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static Pattern RUN_REPORT = Pattern.compile("(\\d+) / (.+)");
  private static Pattern RUN_REPORT_PTN2 = Pattern.compile("(\\d+) /(.*)");
  private static Pattern CODE_CALL = Pattern.compile("(\\d{2}[A-Z]\\d[A-Z]?|CBC)-(.*)");
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.startsWith("CALL CLOSED ")) {
      data.msgType = MsgType.RUN_REPORT;
      Matcher match = RUN_REPORT_PTN2.matcher(body);
      if (!match.matches()) return false;
      setFieldList("ID INFO");
      data.strCallId = match.group(1);
      data.strSupp = match.group(2).replaceAll(" /", "\n").trim();
      return true;
    }
    
    //parse run reports (CALL) ID? PLACE
    if (subject.equals("Message from Dispatch")) {
      setFieldList("ID INFO");
      Matcher mat = RUN_REPORT.matcher(body);
      if (mat.matches()) {
        data.msgType = MsgType.RUN_REPORT;
        data.strCallId = mat.group(1);
        data.strSupp = stripFieldEnd(mat.group(2), "/").replace(" / ", "\n");
      } else {
        data.msgType = MsgType.GEN_ALERT;
        data.strSupp = body;
      }
      return true;
    }
    
    //check subject, trim Nature: tag
    if (!subject.startsWith("Nature:")) return false;
    String field = subject.substring(7).trim();
    //parse CODE-CALL or just CALL
    Matcher mat = CODE_CALL.matcher(field);
    if (mat.matches()) {
      data.strCode = mat.group(1);
      data.strCall = mat.group(2);
    } else data.strCall = field;
    //prepare body to be split
    body = body.replace("Cross:/", "Cross: /"); //in case the / is actually meant to be a delim
    //save outcome, fail if !TIME, return outcome
    if (!parseFields(body.split(" /"), data)) return false;
    return data.strTime.length() > 0; //return false if no TIME parsed
  }
  
  @Override
  public String getProgram() {
    return "CODE CALL " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR_CITY_ST"))  return new MyAddressCityStateField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CITY_ST_ZIP")) return new MyCSZField();
    if (name.equals("PRI")) return new PriorityField("\\d");
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static Pattern ADDR_CITY_ST_PTN = Pattern.compile("(.*), ([ A-Z]+), ([A-Z]{2})");
  private class MyAddressCityStateField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ADDR_CITY_ST_PTN.matcher(field);
      if (!match.matches()) return false;
      parseAddress(match.group(1).trim(), data);
      data.strCity = match.group(2).trim();
      data.strState = match.group(3).trim();
      return true;
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }
  
  private static Pattern ADDR_MAP = Pattern.compile("(.+?)-([A-Z]{1,2})?");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      //sometimes field matches ADDR_MAP/ADDR_MAP. if it does we split and parse both
      int si = field.indexOf("/");
      if (si != -1) {
        
        //parse -XX from each, append second XX if ", " if non redundant
        String head = field.substring(0, si).trim();
        Matcher mat = ADDR_MAP.matcher(head);
        if (mat.matches()) {
          head = mat.group(1).trim();
          data.strMap = getOptGroup(mat.group(2));
        } //tail
        String tail = field.substring(si+1).trim();
        mat = ADDR_MAP.matcher(tail);
        if (mat.matches()) {
          tail = mat.group(1).trim();
          String map = mat.group(2);
          //if map isn't null or redundant, append
          if (map != null && !data.strMap.contains(map)) data.strMap = append(data.strMap, "-", map);
        }
        
        //now we can parse the intersection normally
        super.parse(head+"/"+tail, data);
        return;
      }

      //if no "/", just parse ADDR-MAP
      Matcher mat = ADDR_MAP.matcher(field);
      if (mat.matches()) {
        field = mat.group(1).trim();
        data.strMap = getOptGroup(mat.group(2));
      } super.parse(field, data);
    }

    @Override
    public String getFieldNames() { return super.getFieldNames() + " MAP"; }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      //remove leading 0
      field = stripFieldStart(field, "0 ");
      //parse X-MAP
      Matcher mat = ADDR_MAP.matcher(field);
      if (mat.matches()) {
        field = mat.group(1);
        //append map if neither null nor redundant
        String map = mat.group(2);
        if (map != null && !data.strMap.contains(map)) data.strMap = append(data.strMap, "-", map);
      } //parse normally
      super.parse(field, data);
    }
  }
  
  private static Pattern CITY_STATE_ZIP = Pattern.compile("(.+), ([A-Z]{2}), \\d{5}");
  private class MyCSZField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = CITY_STATE_ZIP.matcher(field);
      if (!mat.matches()) abort();
      //CITY ST
      data.strCity = mat.group(1).trim();
      data.strState = mat.group(2);
    }

    @Override
    public String getFieldNames() { return "CITY ST"; }
  }
  
  private static Pattern INFO_TIME = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");
  private static Pattern INFO_JUNK_PTN = Pattern.compile(".*[a-z].*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      
      // Parse time field
      Matcher mat = INFO_TIME.matcher(field);
      if (mat.matches()) {
        if (data.strTime.length() == 0) data.strTime = field;
        return;
      } 
      
      // User asked to drop everything containing lower case characters, 
      // which sounds drastic, but turns out to work out quite well
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      super.parse(field, data); 
    }

    @Override
    public String getFieldNames() { return "INFO TIME"; }
  }
}
