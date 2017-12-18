package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;

public class MNEdenPrairieParser extends SmartAddressParser {

  public MNEdenPrairieParser() {
    super(CITY_LIST, "EDEN PRAIRIE", "MN");
    
    setFieldList("ADDR APT CITY PLACE UNIT MAP CALL X ID INFO");
  }
  
  @Override
  public boolean keepLeadBreak() {
    return true;
  }
  
  private static final String UNIT_PTN_STR = "(?:[A-Z]{1,3}\\d{1,3}|DUTY|EMT|GENERAL|PAR|\\d{3})\\.*";
  private static Pattern LEAD_UNIT_PTN = Pattern.compile("^(?:" + UNIT_PTN_STR + " +)+");
  private static Pattern TRAIL_UNIT_PTN = Pattern.compile("(?: +" + UNIT_PTN_STR + ")+$");
  private static Pattern ID_INFO_PTN = Pattern.compile("(.*?) (\\d{4}-\\d{8})(?: +(.*))?");
  private static final Pattern QUAD_PTN = Pattern.compile("\\b(Quad \\d+/\\d+) +");
  private static Pattern CITY_SPACER = Pattern.compile("(?<! )(?=[A-Z][a-z])");
  
  @Override
  public boolean parseUntrimmedMsg(String subject, String body, Data data) {
    //check subject
    if (!subject.equals("Dispatch Information")) return false;
    
    // Strip off leading unit
    boolean checkUnit = false;
    Matcher match = LEAD_UNIT_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strUnit = match.group().trim().replace(".", "");
      body = body.substring(match.end());
      checkUnit = true;
    }
    
    // Strip off trailing ID and Unit
    match = ID_INFO_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      data.strCallId = match.group(2);
      data.strSupp = getOptGroup(match.group(3));
    }
    
    // There is almost always a map code.  In the old format it was at the beginning
    // of the alert message.  In the new format, it comes right before the call description
    boolean newFmt = false;
    match =  QUAD_PTN.matcher(body);
    if (match.find()) {
      data.strMap = match.group(1);
      int start = match.start();
      if (start > 0) {
        newFmt = true;
        String call = body.substring(match.end());
        body = body.substring(0,start).trim();
        body = append(data.strUnit, " ", body);
        data.strUnit = "";
        if (data.strUnit.length() == 0) {
          match = TRAIL_UNIT_PTN.matcher(body);
          if (match.find()) {
            data.strUnit = match.group().trim().replace(".", "");
            body = body.substring(0,match.start());
          }
        }
        
        String tmp = FWD_CALL_LIST.getCode(call, true);
        if (tmp != null) { 
          data.strCross = call.substring(tmp.length()).trim();
          call = tmp;
        } else {
          parseAddress(StartType.START_OTHER, FLAG_START_FLD_REQ | FLAG_ONLY_CROSS | FLAG_EMPTY_ADDR_OK | FLAG_ANCHOR_END, call, data);
          call = getStart();
        }
        data.strCall = call;
      } else {
        body = body.substring(match.end());
      }
    } 
    
    // Sometimes the old map code was a city name
    else {
      Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, body);
      String city = res.getCity();
      if (city.length() > 0) data.strMap = city;
      body = res.getLeft();
    }
    
    //  Replace missing blanks
    body = CITY_SPACER.matcher(body).replaceAll(" ");
    
    // If we haven't already identifed a call description for the new format
    // see if we can identify call description at end of text
    if (data.strCall.length() == 0) {
      String call = REV_CALL_LIST.getCode(body, true);
      if (call != null) {
        data.strCall = call;
        body = body.substring(0, body.length()-call.length()).trim();
      }
    }
    
    // If the unit was numeric, and if there was no map entered (very rare) then there is a
    // chance that what we thought was a unit was really part of the address.  So we had better
    // check and see if the address looks like a single street name and if it is, prepend the unit
    checkUnit = checkUnit && data.strMap.length() == 0 && NUMERIC.matcher(data.strUnit).matches();
    
    // And parse address/place/city/call
    int flags = FLAG_PAD_FIELD;
    if (checkUnit) flags |= FLAG_CHECK_STATUS;
    if (!newFmt && data.strCall.length() > 0) flags |= FLAG_ANCHOR_END;
    parseAddress(StartType.START_ADDR, flags, body, data);
    data.strPlace = getPadField();
    if (newFmt) {
      data.strPlace = append(data.strPlace, " - ", getLeft());
    } else if (data.strCall.length() == 0) {
      data.strCall = getLeft();
    }
    
    if (data.strCall.length() == 0) return false;
    if (data.strCallId.length() == 0 && data.strMap.length() == 0) return false;
    
    if (checkUnit && getStatus() < STATUS_INTERSECTION) {
      data.strAddress = append(data.strUnit, " ", data.strAddress);
      data.strUnit = "";
    }
    return true;
  }

  @Override
  public CodeSet getCallList() {
    return FWD_CALL_LIST;
  }

  private static final String[] CALL_LIST = new String[]{
      "911 Hang Up",
      "Alarm - CO No Symptoms",
      "Alarm - Fire",
      "Alarm - Medical Emergency",
      "Alarm - Waterflow",
      "Animal Wildlife Problem",
      "AOA - Fire",
      "Chase (Escape/Flight",
      "Crash MV Injury",
      "Death Investigation",
      "Discharge of Weapon- Police",
      "Domestic",
      "Domestic Assault",
      "Driving Complaint",
      "DWI",
      "Fire - Cut Gas Line",
      "Fire - Fluid Clean Up",
      "FIRE - General Callback",
      "Fire - Misc",
      "Fire - Power Lines Down",
      "Fire - Rescue",
      "Fire - Smell of Gas, Smoke",
      "Fire - Structure Fire",
      "Fire - Vehicle",
      "Lift Assist",
      "Lock Out",
      "Medical - Cardiac",
      "Medical - Drug Overdose",
      "Medical - Other",
      "Mobile Reset",
      "Motorist Assist",
      "Public Assist",
      "Stuck in Elevator",
      "Suicide Attempt/ Threat",
      "SWAT",
      "Test",
      "Test  line 4  line 3  line 2  line 1",
      "Test Police",
      "Test Police  4th line of test  3rd line of test  2nd line of test  this is a test",
      "Test Police  test narrative line 2  test narrative line 1",
      "Theft",
      "TS"
  };
  private static final ReverseCodeSet REV_CALL_LIST = new ReverseCodeSet(CALL_LIST);
  private static final CodeSet FWD_CALL_LIST = new CodeSet(CALL_LIST);
  
  private static String[] CITY_LIST = new String[]{

      // Cities
      "BLOOMINGTON",
      "BROOKLYN CENTER",
      "BROOKLYN PARK",
      "CHAMPLIN",
      "CORCORAN",
      "CRYSTAL",
      "DAYTON",
      "DEEPHAVEN",
      "EDEN PRAIRIE",
      "EDINA",
      "EXCELSIOR",
      "GOLDEN VALLEY",
      "GREENFIELD",
      "GREENWOOD",
      "HANOVER",
      "HOPKINS",
      "INDEPENDENCE",
      "LONG LAKE",
      "LORETTO",
      "MAPLE GROVE",
      "MAPLE PLAIN",
      "MEDICINE LAKE",
      "MEDINA",
      "MINNEAPOLIS",
      "MINNETONKA",
      "MINNETONKA BEACH",
      "MINNETRISTA",
      "MOUND",
      "NEW HOPE",
      "ORONO",
      "OSSEO",
      "PLYMOUTH",
      "RICHFIELD",
      "ROBBINSDALE",
      "ROCKFORD",
      "ROGERS",
      "SHOREWOOD",
      "SPRING PARK",
      "ST ANTHONY VILLAGE",
      "ST BONIFACIUS",
      "ST LOUIS PARK",
      "TONKA BAY",
      "WAYZATA",
      "WOODLAND",

      // Unorganized territory
      "FORT SNELLING",
      
      // Eden County
      "CHANHASSEN"
  };

}
