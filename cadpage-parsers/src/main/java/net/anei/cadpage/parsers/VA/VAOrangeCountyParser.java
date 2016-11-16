package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;



public class VAOrangeCountyParser extends SmartAddressParser {
  
  public VAOrangeCountyParser() {
    super("ORANGE COUNTY", "VA");
    setFieldList("ADDR APT PHONE PLACE ID CALL INFO GPS");
  }
  
  @Override
  public String getFilter() {
    return "orange911@orangecountyva.gov";
  }
  
  private static final Pattern MASTER = Pattern.compile("LOC: (.*?) NATURE:\\s*");
  private static final Pattern ADDR_ID_CALL_PTN = Pattern.compile("(.*?) (\\d{4}-\\d{8}) (.*)");
  private static final Pattern APT_PTN1 = Pattern.compile("(\\d{1,4}[A-Za-z]?|[A-Za-z]$)\\b *(.*)");
  private static final Pattern APT_PTN2 = Pattern.compile("(.*)? (\\d{1,4}[A-Za-z]?|[A-Za-z])");
  private static final Pattern PHONE_PTN = Pattern.compile("(\\d{10})\\b *(.*)");
  private static final Pattern TRAIL_GPS_PTN = Pattern.compile("\\\\+ *(?:([-+]?\\d{2,3}\\.\\d{6,} +[-+]?\\d{2,3}\\.\\d{6,})|-361 -361)(?: +[- A-Z0-9]+)?$");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Orange911")) return false;
    
    Matcher match = MASTER.matcher(body);
    if (!match.lookingAt()) return false;
    String addr = match.group(1).trim();
    String info = body.substring(match.end());
    
    match = ADDR_ID_CALL_PTN.matcher(addr);
    if (match.matches()) {
      addr = match.group(1).trim();
      data.strCallId = match.group(2);
      data.strCall = match.group(3).trim();
    } else {
      String call = CALL_LIST.getCode(addr, true);
      if (call != null) {
        data.strCall = call;
        addr = addr.substring(0, addr.length()-call.length()).trim();
      }
    }
    
    parseAddress(StartType.START_ADDR, addr, data);
    String left = getLeft();
    
    match = APT_PTN1.matcher(left);
    if (match.matches()) {
      data.strApt = append(data.strApt, "-", match.group(1));
      left = match.group(2);
    }
    
    match = PHONE_PTN.matcher(left);
    if (match.matches()) {
      data.strPhone = match.group(1);
      left = match.group(2);
    }
    
    if (data.strCall.length() == 0) {
      data.strCall = left;
    } else {
      match = APT_PTN2.matcher(left);
      if (match.matches()) {
        left = match.group(1).trim();
        data.strApt = append(data.strApt, "-", match.group(2));
      }
      data.strPlace = left;
    }
    
    match = TRAIL_GPS_PTN.matcher(info);
    if (match.find()) {
      String gps = match.group(1);
      if (gps != null) setGPSLoc(gps, data);
      info = info.substring(0,match.start()).trim();
    }
    
    int pt = info.indexOf("E911 Info - ");
    if (pt >= 0) info = info.substring(0,pt).trim();
    data.strSupp = info;

    return true;
  }
  
  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }
  
  private static final CodeSet CALL_LIST = new ReverseCodeSet(
      "AA- Auto Accident",
      "AA- HAZ ONLY",
      "AA- Unk/No Injuries MRP",
      "AA- W/ ENT",
      "Abdominal Pain",
      "Allergic Reaction",
      "ALOC/AMS",
      "Assist LE",
      "Back Pain",
      "Brush Fire",
      "Cardiac Arrest/CPR",
      "CardiacEmergency/MI",
      "Child lockout vehicle",
      "Choking",
      "CO Alarm",
      "CP",
      "DB",
      "Diabetic Emergency",
      "Dumpster Fire",
      "Electrocution",
      "Fire Alarm COM",
      "Fire Alarm NHSA",
      "Fire Alarm RES",
      "Fuel Spill Small",
      "Gas Leak Outside",
      "Illness",
      "Injury",
      "Injury/Fall",
      "Lift Assist",
      "Lines Down",
      "Medical Alarm",
      "Minor Bleeding",
      "NEW CALL",
      "Odor Inside COM",
      "Odor Inside RES",
      "Odor Outside",
      "Outside Fire",
      "Outside Smoke",
      "Overdose/Poisoning",
      "Pedestrian Struck",
      "Public Service EMS",
      "Public Service Fire",
      "Road Hazard",
      "Seizure",
      "Severe Bleeding",
      "Smell of Smoke RES",
      "Standby EMS",
      "Standby FIRE",
      "Stroke",
      "Structure Fire COM",
      "Structure Fire NHSA",
      "Structure Fire RES",
      "Structure Fire SHED/BARN",
      "Suicide/Attempted",
      "Syncopal Episode",
      "Transfer",
      "Unconscious",
      "UNK Medical Emergency",
      "Vehicle Fire"
  );
  
  private static final String[] CITY_LIST = new String[]{
    "GORDONSVILLE",
    "ORANGE",
    "BARBOURSVILLE",
    "LOCUST GROVE",
    
    "MADISON COUNTY",
    "CULPEPER COUNTY",
    "SPOTSYLVANIA COUNTY",
    "LOUISA COUNTY",
    "ALBEMARLE COUNTY",
    "GREENE COUNTY"
  };
}
