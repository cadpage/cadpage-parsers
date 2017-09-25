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
    
    setFieldList("UNIT MAP ADDR APT PLACE CITY CALL ID INFO");
  }
  
  @Override
  public boolean keepLeadBreak() {
    return true;
  }
  
  private static Pattern UNIT_PTN = Pattern.compile("(?:(?:[A-Z]{1,2}\\d{1,3}|DUTY|EMT|\\d{3})\\.* +)+");
  private static Pattern ID_INFO_PTN = Pattern.compile("(.*?) (\\d{4}-\\d{8})(?: +(.*))?");
  private static final Pattern QUAD_PTN = Pattern.compile("(Quad \\d+/\\d+) +");
  private static Pattern CITY_SPACER = Pattern.compile("(?<! )(?=[A-Z][a-z])");
  
  @Override
  public boolean parseUntrimmedMsg(String subject, String body, Data data) {
    //check subject
    if (!subject.equals("Dispatch Information")) return false;
    
    // Strip off leading unit
    Matcher match = UNIT_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strUnit = match.group().trim().replace(".", "");
      body = body.substring(match.end());
    }
    
    // Strip off trailing ID and Unit
    match = ID_INFO_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      data.strCallId = match.group(2);
      data.strSupp = getOptGroup(match.group(3));
    }
    
    // There is **almost** always a map code at the begining of the alert
    // that will be either a quadrant or a recognized city name
    match =  QUAD_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strMap = match.group(1);
      body = body.substring(match.end());
    } else {
      Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, body);
      String city = res.getCity();
      if (city.length() > 0) data.strMap = city;
      body = res.getLeft();
    }
    
    //  Replace missing blanks
    body = CITY_SPACER.matcher(body).replaceAll(" ");
    
    // See if we can identify call description at end of text
    String call = CALL_LIST.getCode(body, true);
    if (call != null) {
      data.strCall = call;
      body = body.substring(0, body.length()-call.length()).trim();
    }
    
    // If the unit was numeric, and if there was no map entered (very rare) then there is a
    // chance that what we thought was a unit was really part of the address.  So we had better
    // check and see if the address looks like a single street name and if it is, prepend the unit
    boolean checkUnit = data.strMap.length() == 0 && NUMERIC.matcher(data.strUnit).matches();
    
    // And parse address/place/city/call
    int flags = FLAG_PAD_FIELD;
    if (checkUnit) flags |= FLAG_CHECK_STATUS;
    if (data.strCall.length() > 0) flags |= FLAG_ANCHOR_END;
    parseAddress(StartType.START_ADDR, flags, body, data);
    data.strPlace = getPadField();
    if (data.strCall.length() == 0) data.strCall = getLeft();
    
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
    return CALL_LIST;
  }

  private static final ReverseCodeSet CALL_LIST = new ReverseCodeSet(
      "911 Hang Up",
      "Alarm - CO No Symptoms",
      "Alarm - Fire",
      "Alarm - Waterflow",
      "AOA - Fire",
      "Crash MV Injury",
      "Fire - Fluid Clean Up",
      "FIRE - General Callback",
      "Fire - Misc",
      "Fire - Smell of Gas, Smoke",
      "Fire - Structure Fire",
      "Fire - Vehicle",
      "Lift Assist",
      "Medical - Cardiac",
      "Medical - Other",
      "Mobile Reset",
      "Public Assist",
      "Stuck in Elevator",
      "Suicide Attempt/ Threat",
      "Test  line 4  line 3  line 2  line 1",
      "Test Police",
      "Test Police  4th line of test  3rd line of test  2nd line of test  this is a test",
      "Test Police  test narrative line 2  test narrative line 1",
      "Theft"
  );
  
  private static String[] CITY_LIST = new String[]{
      "BLOOMINGTON",
      "EDEN PRAIRIE",
      "EDINA",
      "MINNETONKA"
  };

}
