package net.anei.cadpage.parsers.MI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class MIBayCountyAParser extends SmartAddressParser {
  
  private static final Pattern RUN_REPORT_ID_PTN = Pattern.compile("ID:(.*)\n");
  private static final Pattern RUN_REPORT_UNIT_PTN = Pattern.compile("UNIT:(.*)\n");
  private static final Pattern MASTER = Pattern.compile("([^,]+?)(?:, (.*?))?(?: +(?:CALL|ALL|LL|L)\\.+|\\.*)? ?(1?\\d/\\d\\d?.\\d{4} \\d\\d?:\\d\\d?:\\d\\d [AP]M)");
  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
  
  public MIBayCountyAParser() {
    super(CITY_LIST, "BAY COUNTY", "MI");
    setFieldList("CALL ADDR APT CITY INFO DATE TIME");
    setupCallList(CALL_SET);
    removeWords("STREET");
  }
  
  @Override
  public String getFilter() {
    return "911@baycounty.net";
  }
  
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("CALL: RUN REPORT \n")) {
      data.strCall = "RUN REPORT";
      data.strPlace = body;
      Matcher match = RUN_REPORT_ID_PTN.matcher(body);
      if (match.find()) data.strCallId = match.group(1).trim();
      match = RUN_REPORT_UNIT_PTN.matcher(body);
      if (match.find()) data.strUnit = match.group(1).trim();
      return true;
    }
    
    body = body.replace('\n', ' ');
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    String addr = match.group(1).trim();
    addr = addr.replace('@', '&');
    String cityInfo = match.group(2);
    setDateTime(DATE_TIME_FMT, match.group(3), data);
    
    if (cityInfo != null) {
      int pt = addr.indexOf("  ");
      if (pt >= 0) {
        data.strCall = addr.substring(0,pt).trim();
        parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_NO_CITY  | FLAG_ANCHOR_END, addr.substring(pt+2).trim(), data);
      } else {
        parseAddress(StartType.START_CALL, FLAG_RECHECK_APT | FLAG_NO_CITY | FLAG_ANCHOR_END, addr, data);
      }
      
      pt = cityInfo.indexOf("  ");
      if (pt >= 0) {
        data.strCity = cityInfo.substring(0,pt).trim().replace(".", "");
        data.strSupp = cityInfo.substring(pt+2).trim();
      } else {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, cityInfo, data);
        data.strSupp = getLeft();
      }
    }
    
    // When there is no comma delimited city :(
    else {
      Parser p = new Parser(addr);
      String p1 = p.get("  ");
      String p2 = p.get("  ");
      String p3 = p.get();
      if (p3.length() > 0) {
        data.strCall = p1;
        parseAddress(p2, data);
        data.strSupp =  p3;
      } else if (p2.length() > 0) {
        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_RECHECK_APT | FLAG_ANCHOR_END, p1, data);
        data.strSupp = p2;
      } else {
        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_RECHECK_APT, p1, data);
        data.strSupp = getLeft();
      }
    }
    
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "AUBURN",
    "BAY CITY",
    "ESSEXVILLE",
    "PINCONNING",
    "MIDLAND",
    
    // Unincorporated
    "BENTLEY",
    "CRUMP",
    "DUEL",
    "KAWKAWLIN",
    "LINWOOD",
    "MOUNT FOREST",
    "MUNGER",
    "WILLARD",
    "UNIVERSITY CENTER",

    // Townships
    "BANGOR TWP",
    "BEAVER TWP",
    "FRANKENLUST TWP",
    "FRASER TWP",
    "GARFIELD TWP",
    "GIBSON TWP",
    "HAMPTON TWP",
    "KAWKAWLIN TWP",
    "MERRITT TWP",
    "MONITOR TWP",
    "MT FOREST TWP",
    "PINCONNING TWP",
    "PORTSMOUTH TWP",
    "WILLIAMS TWP"
  };
  
  private static final CodeSet CALL_SET = new CodeSet(
      "CITIZEN",
      "CO SICK O",
      "CO ONLY",
      "COMFIRE",
      "FIRE ALARM",
      "GAS IN",
      "MED",
      "PIA",
      "POLE",
      "SMOKE",
      "STRUCTURE",
      "SUICIDE",
      "TEST",
      "VEH FIRE",
      "WIRE"
  );
}
