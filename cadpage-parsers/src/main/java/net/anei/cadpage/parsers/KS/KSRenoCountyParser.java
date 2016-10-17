package net.anei.cadpage.parsers.KS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

/**
 * Reno County, KS
 */
public class KSRenoCountyParser extends SmartAddressParser {
  
  public KSRenoCountyParser() {
    super(CITY_LIST, "RENO COUNTY", "KS");
    setFieldList("CALL ADDR APT CITY GPS INFO ID");
  }
  
  @Override
  public String getFilter() {
    return "NWSPage@renolec.com";
  }  
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern MASTER = Pattern.compile("(.*?)  -  (.*?)(?:  (\\d{4}-\\d{8}))?");
  private static final Pattern GPS_PTN = Pattern.compile(" (\\d+\\.\\d{6,}|-361) / (-\\d+\\.\\d{6,}|-361)(?:  |$)");
  private static final Pattern AVE_X_PTN = Pattern.compile("(.*\\b[EW] AVE) & ([A-Z])");
  private static final Pattern KDD_HWY_PTN = Pattern.compile("\\bK(\\d+) HWY\\b");
  private static final Pattern E911_INFO_PTN = Pattern.compile(" *\\bE911 Info - Class of Service: [A-Za-z0-9]+ Special Response Info: UNC=.* Uncertainty: +Confidence: *");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Page Notification")) return false;
    
    body = body.replace("\n ", "").replace("\n", "");
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    body = match.group(2).trim();
    data.strCallId = getOptGroup(match.group(3));
    
    match = GPS_PTN.matcher(body);
    if (match.find()) {
      setGPSLoc(match.group(1)+','+match.group(2), data);
      String addr = body.substring(0,match.start()).trim();
      parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr, data);
      data.strSupp = body.substring(match.end()).trim();
    } else {
      parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_RECHECK_APT, body, data);
      data.strSupp = getLeft();
    }
    
    // Weird E AVE X street names get picked up by the implied intersection logic and
    // have to be corrected
    match = AVE_X_PTN.matcher(data.strAddress);
    if (match.matches()) {
      data.strAddress = match.group(1) + ' ' + match.group(2);
    }
    
    // Kxx HWY needs to turn into KS xx
    data.strAddress = KDD_HWY_PTN.matcher(data.strAddress).replaceAll("KS $1");
    
    data.strSupp = E911_INFO_PTN.matcher(data.strSupp).replaceAll(" ").trim();
    
    if (data.strCity.equalsIgnoreCase("COUNTY")) data.strCity = "";
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "ABBYVILLE",
    "ARLINGTON",
    "BUHLER",
    "HAVEN",
    "HUTCHINSON",
    "LANGDON",
    "NICKERSON",
    "PARTRIDGE",
    "PLEVNA",
    "PRETTY PRAIRIE",
    "SOUTH HUTCHINSON",
    "SYLVIA",
    "TURON",
    "WILLOWBROOK",

    // Unincorporated communities
    "CASTLETON",
    "DARLOW",
    "MEDORA",
    "PLEASANTVIEW",
    "ST JOE",
    "YODER",
   
    // Townships
    "ALBION TWP",
    "BELL TWP",
    "CASTLETON TWP",
    "CENTER TWP",
    "CLAY TWP",
    "ENTERPRISE TWP",
    "GRANT TWP",
    "GROVE TWP",
    "HAVEN TWP",
    "HAYES TWP",
    "HUNTSVILLE TWP",
    "LANGDON TWP",
    "LINCOLN TWP",
    "LITTLE REVIER TWP",
    "LODA TWP",
    "MEDFORD TWP",
    "MEDORA TWP",
    "MIAMI TWP",
    "NINNESCAH TWP",
    "PLEVNA TWP",
    "RENO TWP",
    "ROSCOE TWP",
    "SALT CREEK TWP",
    "SUMNER TWP",
    "SYLVIA TWP",
    "TROY TWP",
    "VALLEY TWP",
    "WALNUT TWP",
    "WESTMINISTER TWP",
    "YODER TWP",
    
    "COUNTY"
  };
}
