package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;
/**
 * Craven County, NC
 * Obsolete as of 9/15/14 - replaced by NCCravenCountyCParser
 */
public class NCCravenCountyBParser extends SmartAddressParser {
  
  private static final Pattern UNIT_PTN = Pattern.compile("(?:(?:EMSA|STA ?\\d+[A-Z]?|Headquarters) +)+");
  private static final Pattern ID_PTN = Pattern.compile("\\b\\d{4}-\\d{8}\\b");
  private static final Pattern GPS_PTN = Pattern.compile("\\s*(?:E911 Info .*)?(?:\\b(3[45]\\.\\d{4,} +-7[67]\\.\\d{4,})|-361 +-361)\\b");
  private static final Pattern OFF_HWY_PTN = Pattern.compile("OFF +HWY +\\d+\\b");
  
  public NCCravenCountyBParser() {
    super(CITY_LIST, "CRAVEN COUNTY", "NC");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@cravencountync.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CC911")) return false;
    if (body.startsWith("Location:")) return false;
    if (internalParseMsg(body, data)) {
      if (data.strCity.equalsIgnoreCase("Jones")) data.strCity = "Jones County";
      return true;
    } else {
      data.parseGeneralAlert(this, body);
      return true;
    }
  }
  
  private boolean internalParseMsg(String body, Data data) {
    
    // Parse option unit field from front of text
    Matcher match = UNIT_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strUnit = match.group().trim();
      body = body.substring(match.end());
    }
    
    // There always has to be a call ID somewhere
    match = ID_PTN.matcher(body);
    if (!match.find()) return false;
    
    data.strCallId = match.group();
    String addr = body.substring(0,match.start()).trim();
    String trail = body.substring(match.end()).trim();
    
    // There may or may not be some GPS coordinates in the trailing section
    // If there are, use the smart address parser to identify the address and
    // call in the first section
    match = GPS_PTN.matcher(trail);
    if (match.find()) {
      setFieldList("UNIT ADDR APT CITY CALL ID INFO GPS");
      String gps = match.group(1);
      if (gps != null) setGPSLoc(gps,data);
      data.strSupp = append(trail.substring(0,match.start()).trim(), " ", trail.substring(match.end()).trim());
      return parseAddressAndCall(addr, data);
    }
    
    // If we find GPS coordinates in the forward section, they separate the
    // address from the call description
    setFieldList("UNIT ADDR APT CITY GPS CALL ID INFO");
    data.strSupp = trail;
    match = GPS_PTN.matcher(addr);
    if (match.find()) {
      String gps = match.group(1);
      if (gps != null) setGPSLoc(gps,data);
      parseOnlyAddress(addr.substring(0,match.start()).trim(), data);
      data.strCall = addr.substring(match.end()).trim();
      return true;
    }
    
    return parseAddressAndCall(addr, data);
  }

  private boolean parseAddressAndCall(String addr, Data data) {
    String call = CALL_SET.getCode(addr);
    if (call != null) {
      data.strCall = call;
      addr = addr.substring(0,addr.length()-call.length()).trim();
      parseOnlyAddress(addr, data);
      return true;
    }
    
    Matcher match = OFF_HWY_PTN.matcher(addr);
    if (match.lookingAt()) {
      data.strAddress = match.group();
      data.strCall = addr.substring(match.end()).trim();
    } else {
      parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT, addr, data);
      data.strCall = getLeft();
    }
    return (data.strCall.length() > 0);
  }
  
  private void parseOnlyAddress(String addr, Data data) {
    Matcher match = OFF_HWY_PTN.matcher(addr);
    if (match.matches()) {
      data.strAddress = addr;
    } else {
      parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, addr, data);
    }
  }
  
  @Override
  public CodeSet getCallList() {
    return CALL_SET;
  }

  private static CodeSet CALL_SET = new ReverseCodeSet(
      "911",
      "Alarm Business",
      "Assault",
      "Check on welfare",
      "Citizen Assist",
      "Concealed Handgun Permit",
      "Death",
      "Disturbance/Unknown Type",
      "Fire Alarm Business",
      "Fire Alarm Residence",
      "Fire Forest/Woods",
      "Fire Grass/Brush",
      "Fire Hazardous Cond. Gas Oil",
      "Fire Illegal Burning",
      "Fire Structure",
      "Fire Trash",
      "Fire Unknown Size or Type",
      "Fire Vehicle",
      "Gas Leak Confirmed",
      "Gas Leak Non-confirmed",                                                                                                                                              
      "Medical",                                                                                                                                                             
      "Mental Subject",                                                                                                                                                      
      "Missing",                                                                                                                                                             
      "Mutual Aid Request",                                                                                                                                                  
      "New Call",                                                                                                                                                            
      "Person Complaint",                                                                                                                                                    
      "Special Operation",
      "Stranded Motorist",                                                                                                                                                   
      "Suicide/Attempted",                                                                                                                                                   
      "Susp Vehicle",                                                                                                                                                        
      "Test Screen",
      "Traffic All Other",    
      "TS",                                                                                                                                                                  
      "Veh Crash-Injury",                                                                                                                                                    
      "Veh Crash-Property",                                                                                                                                                  
      "Water Rescue"                                                                                                                                                        
  );
  
  private static final String[] CITY_LIST = new String[]{
    "BRICES CREEK",
    "BRIDGETON",
    "COVE CITY",
    "DOVER",
    "ERNUL",
    "FAIRFIELD HARBOUR",
    "HAVELOCK",
    "JAMES CITY",
    "NEUSE FOREST",
    "NEW BERN",
    "RIVER BEND",
    "TRENT WOODS",
    "VANCEBORO",
    "EMUL",
    "HARLOWE",
    "CHERRY POINT",
    "CHERRY BRANCH",
    "ADAMS CREEK",
    "FORT BARNWELL",
    
    "JONES",   // Jones County
    
    // Lenoir County
    "GRIFTON"
  };

}
