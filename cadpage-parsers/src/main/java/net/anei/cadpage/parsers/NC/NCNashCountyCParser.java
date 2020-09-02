package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class NCNashCountyCParser extends FieldProgramParser {
  
  public NCNashCountyCParser() {
    super(NCNashCountyParser.CITY_LIST, "NASH COUNTY", "NC", 
          "CALL CALL2? ADDRCITY MISC? PLACE GPS1 GPS2 NAME? UNIT/C+? ID DATETIME! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "donotreply@nashcountync.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private String tmpSubject;
  private String times;
  
  private static final Pattern FIND_DELIM_PTN = Pattern.compile("\\bCFS\\d\\d-\\d{6}([:;])");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    tmpSubject = subject;
    
    // They switched delimiters from a semicolon to a colon-space recently
    // We still want to process both formats, but spurious colons and semicolons make the
    // usual approach of trying both and seeing which one has the most fields unfeasible.  So
    // instead, look for the required ID field and see what delimiter follows it.
    Matcher match = FIND_DELIM_PTN.matcher(body);
    if (!match.find()) return false;
    String delim = match.group(1);
    if (delim.equals(":")) {
      delim += ' ';
      if (body.endsWith(":")) body += ' ';
      body = body.replace(" RM: ", " RM ").replace(" FLOOR: ", " FLOOR ");
    }
    getMiscField().reset();
    times = "";
    if (!parseFields(body.split(delim), data)) return false;
    data.strUnit = data.strUnit.replace("; ", ",");
    if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(data.strSupp, "\n", times);
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new CallField("([-A-Z]+)|None()", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("MISC")) return getMiscField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("NAME")) return new NameField("()None|([^0-9;]+|.*[^;] .*)", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ID"))  return new IdField("CFS\\d\\d-\\d{6}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d?:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("(.*?), *([A-Z]{2})\\b(?: +(\\d{5}))?(?: (.*?))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.length() == 0) data.strCall = tmpSubject;

      String callExt;
      int pt = field.indexOf(',');
      if (pt >= 0) {
        parseAddress(field.substring(0,pt).trim(), data);
        field = field.substring(pt+1).trim();
        
        Matcher match = ADDR_ST_ZIP_PTN.matcher(field);
        if (match.matches()) {
          data.strCity = match.group(1).trim();
          data.strState = match.group(2);
          callExt = getOptGroup(match.group(4));
        } else {
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, field, data);
          callExt = getLeft();
        }
      }
      
      else {
        Result res = parseAddress(StartType.START_ADDR, field);
        if (res.isValid()) {
          res.getData(data);
          callExt = res.getLeft();
        } else {
          Matcher match = MISC_START_PTN.matcher(field);
          if (match.find()) {
            callExt = field.substring(match.start());
            field = field.substring(0,match.start()).trim();
          } else {
            callExt = "";
          }
          parseAddress(field, data);
        }
      }
      
      data.strCity = convertCodes(data.strCity, NCNashCountyParser.CITY_FIXES);
      if (callExt.length() > 0) getMiscField().parse(callExt, data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY ST " + getMiscField().getFieldNames();
    }
  }
  
  
  private MyMiscField miscField;
  
  public MyMiscField getMiscField() {
    if (miscField == null) miscField = new MyMiscField();
    return miscField;
  }

  private static final Pattern MISC_TAC_PTN = Pattern.compile("(.*?)[- /]*((?:FRANKLIN COUNTY )?TAC[- /]*\\d*)[-/ ]*(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern MISC_APT_PTN = Pattern.compile("(.*?)[- /]*\\b(?:APT|ROOM|RM|LOT|UNI?T) +(?!IS)(\\S+) *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern MISC_START_PTN = Pattern.compile("\\b(?:(?:FRANKLIN COUNTY )?TAC|APT|ROOM|RM|LOT|UNI?T)", Pattern.CASE_INSENSITIVE);
  private class MyMiscField extends Field {
    
    private boolean used;
    
    public void reset() {
      used = false;
    }
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (used) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      used = true;
      if (field.equals("None")) return;
      Matcher match = MISC_TAC_PTN.matcher(field);
      if (match.matches()) {
        data.strChannel = match.group(2);
        field = append(match.group(1), " ", match.group(3));
      }
      match = MISC_APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(2));
        field = append(match.group(1), " ", match.group(3));
      }
      data.strCall = append(data.strCall, " - ", field);
    }
    
    @Override
    public String getFieldNames() {
      return "CH APT CALL";
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("(\\S+): *(.*)");
  private static final Pattern UNIT_TIME_PTN = Pattern.compile("(?:Assigned|Enroute|Arrived|Completed) *(.*)");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = UNIT_PTN.matcher(field);
      if (match.matches()) {
        data.strUnit = append(data.strUnit, ",", match.group(1));
        times = append(times, "\n", match.group(2));
        return;
      }
      match = UNIT_TIME_PTN.matcher(field);
      if (match.matches()) {
        if (match.group(1).length() == 0) return;
        times = append(times, "\n", field);
        if (field.startsWith("Completed")) data.msgType = MsgType.RUN_REPORT;
        return;
      }
      data.strUnit = append(data.strUnit, ",", field);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
}
