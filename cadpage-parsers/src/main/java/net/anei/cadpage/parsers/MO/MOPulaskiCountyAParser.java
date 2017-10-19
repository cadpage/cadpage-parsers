package net.anei.cadpage.parsers.MO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.ReverseCodeSet;


/**
 * Pulaski County, MO
 */
public class MOPulaskiCountyAParser extends FieldProgramParser {
  
  public MOPulaskiCountyAParser() {
    super(CITY_TABLE, "PULASKI COUNTY", "MO",
           "ADDR/S CrossStreets:X! Call_Received_Time:SKIP Dispatch:DATETIME Dispatch:SKIP");
  }
  
  @Override
  public String getFilter() {
    return "911dispatch@embarqmail.com,messaging@iamresponding.com";
  }
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("Call Number: *(\\d+) +Call Received Time: *(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?) (.*?)\n(.*?) (Dispatch:.*)");
  private static final Pattern RUN_REPORT_BRK_PTN = Pattern.compile(" +(?=[A-Z][a-z.]+:)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  
  String saveAddress;
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("ID DATE TIME CALL UNIT INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strDate = match.group(2);
      String time = match.group(3);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
      data.strCall = match.group(4).trim();
      data.strUnit = match.group(5).trim();
      data.strSupp = RUN_REPORT_BRK_PTN.matcher(match.group(6).trim()).replaceAll("\n");
      return true;
    }
    
    saveAddress = null;
    if (!super.parseMsg(body, data)) return false;
    
    // In the old format, the call was imbedded in the address field
    // and not the cross street field.  If we didn't find a call description
    // re-parser the address field using the old logic
    if (data.strCall.length() == 0) {
      if (saveAddress == null) return false;
      data.strAddress = data.strApt = "";
      saveAddress = saveAddress.replace(", ", " ");
      String[] parts = saveAddress.split("  +");
      if (parts.length == 3) {
        data.strCall = parts[0];
        parseAddress(parts[1], data);
        data.strCity = parts[2];
      } else if (parts.length == 2) {
        data.strCall = parts[0];
        parseAddress(StartType.START_ADDR, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, parts[1], data);
      } else {
        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, saveAddress, data);
      }

      data.strAddress = stripFieldEnd(data.strAddress, "UNIT");
      data.strApt = stripFieldEnd(data.strApt, "UNIT");
      
      int pt = data.strAddress.indexOf(" - ");
      if (pt >= 0) {
        data.strPlace = data.strAddress.substring(pt+3).trim();
        data.strAddress = data.strAddress.substring(0,pt).trim();
      }
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      saveAddress = field;
      super.parse(field, data);
    }
  }
  
  // Unit codes will be nnnn, or xFDn, or Mnn
  private static final Pattern UNIT_PTN = Pattern.compile("(?: *\\b(?:[A-Z]+\\d+|\\d{3,4}|[A-Z]{1,2}[FP]D\\d?|CAMDEN|DAD|EVAC|FLWAMB|LACLEDE|LIFELINE|MARIES|MILLER|RESCUE|SLEEPER|STAFF)\\b)+$");
  
  // Place/Call portion never has any lower case characters
  private static final Pattern CROSS_CALL_PTN = Pattern.compile("(.*?[a-z.].*?) (?![NS]?[EW]?(?: |$))([^a-z.]+)"); 
  private static final Pattern CROSS_PTN = Pattern.compile(".*?[a-z.].*"); 
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      
      // Extract unit codes from end of field
      Matcher match = UNIT_PTN.matcher(field);
      if (match.find()) {
        data.strUnit = match.group(0).trim();
        field = field.substring(0,match.start()).trim();
      }
      
      // Split out the lower case cross street information
      match = CROSS_CALL_PTN.matcher(field);
      if (match.matches()) {
        super.parse(match.group(1).trim(), data);
        field = match.group(2).trim();
      }
      
      else if (CROSS_PTN.matcher(field).matches()) {
        super.parse(field, data);
        return;
      }
      
      // Split what is left into place name and call description
      String call = CALL_LIST.getCode(field, true);
      if (call != null) {
        data.strCall = call;
        data.strPlace = field.substring(0,field.length()-call.length()).trim();
      } else {
        data.strCall = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "X PLACE CALL UNIT";
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = HIST_PTN.matcher(addr).replaceAll("HISTORIC");
    addr = HIST_RT_PTN.matcher(addr).replaceAll("HISTORIC US");
    return addr;
  }
  private static final Pattern HIST_PTN = Pattern.compile("\\bHIST\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern HIST_RT_PTN = Pattern.compile("HISTORIC RT", Pattern.CASE_INSENSITIVE);
  
  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }
  
  private static final ReverseCodeSet CALL_LIST = new ReverseCodeSet(
      "ABDOMINAL/BACK PAIN",
      "ALARM",
      "ALLERGIC REACTION",
      "BLEEDING (NON-TRAUMATIC)",
      "BREATHING DIFFICULTY",
      "BURN PERMIT",
      "CARBON MONOXIDE ALARM",
      "CHEST PAIN/DISCOMFORT/HEART PROBLEMS",
      "DIABETIC",
      "FALLS/ ACCIDENTS",
      "FIRE ALARM - COMMERCIAL",
      "FIRE ALARM - RESIDENTIAL",
      "FIRE - MISCELLANEOUS",
      "GAS ODOR",
      "GAS ODOR/LEAK RESIDENTIAL",
      "HAZARDOUS MATERIAL INCIDENT",
      "HAZMAT - FUEL SPILL",
      "LIFT ASSIST",
      "LINES DOWN",
      "MENTAL/EMOTIONAL/PSYCHOLOGICAL",
      "MOTOR VEHICLE ACCIDENT NO INJURY",
      "MOTOR VEHICLE ACCIDENT WITH INJURY",
      "NATURAL COVER FIRE",
      "NEUROLOGICAL/HEAD INJURY",
      "OVERDOSE/POISONING",
      "OTHER",
      "SEIZURES",
      "SMOKE INVESTIGATION",
      "SMOKE ODOR",
      "STROKE (CVA)",
      "STRUCTURE FIRE COMMERCIAL",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE RESIDENTIAL",
      "SUICIDE ATTEMPTED",
      "SYNCOPE",
      "TRAFFIC STOP",
      "TRAUMA WITH INJURY",
      "UNCLEAR SYMPTOMS",
      "UNCONSCIOUS/UNRESPONSIVE",
      "VEHICLE FIRE",
      "WEATHER",
      
      // Supporting old call format
      "FIRE ALARM - COMMERCIAL MASONIC LODGE - WAYNESVILLE",
      "FIRE ALARM - COMMERCIAL WESTSIDE BAPTIST CHURCH",
      "BUCKHORN P"
  );
  
  private static final String[] CITY_TABLE = new String[]{
    "PULASKI COUNTY",
    "BIG PINEY",
    "CROCKER",
    "DEVILS ELBOW",
    "DIXON",
    "FORT LEONARD WOOD",
    "GASCOZARK",
    "HOOKER",
    "LAQUEY",
    "RICHLAND",
    "ST ROBERT",
    "SWEDEBORG",
    "WAYNESVILLE",
    
    "CAMDEN COUNTY",
    "LACLEDE COUNTY",
    "MARIES COUNTY",
    "MILLER COUNTY",
    "PHELPS COUNTY",
    "TEXAS COUNTY"
  };
}
