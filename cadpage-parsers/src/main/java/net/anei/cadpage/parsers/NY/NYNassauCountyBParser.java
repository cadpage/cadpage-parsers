package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NYNassauCountyBParser extends SmartAddressParser {

  public NYNassauCountyBParser() {
    super("NASSAU COUNTY", "NY");
    addExtendedDirections();
    setFieldList("PLACE ADDR X MAP CALL INFO DATE TIME ID");
    setupMultiWordStreets(
        "BRYN MAWR",
        "HEWLETT NECK",
        "HYDE PARK",
        "IVY HILL",
        "WEST END"
    );
  }
  
  public String getFilter() {
    return "paging@alpinesoftware.com,@rednmxcad.com";
  }
  
  private static final Pattern NON_ASCII_PTN = Pattern.compile("[^\\p{ASCII}]");
  private static final Pattern MARKER = Pattern.compile("^(?:FireCom:|RedAlert:)");
  private static final Pattern CALL_PTN = Pattern.compile(" *\\(([-/ A-Z0-9]{2,})\\) *");
  private static final Pattern MAP_PTN = Pattern.compile(" - +(?:(?:AREA|MAP|ZONE) +)?(.*)$");
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d) (\\d\\d:\\d\\d)");
  private static final Pattern MAP_PREFIX_PTN = Pattern.compile("[- ]+");
  
  // Pattern to catch problematic patterns, typically those that contain numbers
  // that might be mistaken for street numbers
  private static final Pattern PLACE_PTN = 
      Pattern.compile("(.* NUMBER \\d+ SCHOOL|.* FD STATION \\d+|ELKS BPOE \\d+) +(.*)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    body = NON_ASCII_PTN.matcher(body).replaceAll("");
    
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    body = body.substring(match.end()).trim();
    
    match = CALL_PTN.matcher(body);
    if (!match.find()) return false;
    String sAddr = body.substring(0, match.start());
    data.strCall = match.group(1).trim();
    String sExtra = body.substring(match.end());
    
    match = MAP_PTN.matcher(sAddr);
    if (match.find()) {
      String map = match.group(1);;
      sAddr = sAddr.substring(0,match.start()).trim();
      match = MAP_PREFIX_PTN.matcher(map);
      if (match.lookingAt()) map = map.substring(match.end());
      data.strMap = map;
    } else {
      sAddr = stripFieldEnd(sAddr, " -");
    }
    match = PLACE_PTN.matcher(sAddr);
    StartType st = StartType.START_PLACE;
    if (match.matches()) {
      data.strPlace = match.group(1);
      sAddr = match.group(2);
    }
    parseAddress(st, FLAG_IGNORE_AT | FLAG_ANCHOR_END, sAddr, data);
    
    data.strCross = stripFieldEnd(data.strCross, "/");
    
    Parser p = new Parser(' ' + sExtra);
    data.strSupp = p.get(" D:");
    String dateTime = p.get(" #:");
    data.strCallId = p.get();
    
    match = DATE_TIME_PTN.matcher(dateTime);
    if (match.matches()) {
      data.strDate = match.group(1);
      data.strTime = match.group(2);
    }

    return true;
  }
}


