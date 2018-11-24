package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SmartAddressParser;


public class SCClarendonCountyParser extends SmartAddressParser {
  
  private static final Pattern STATUS_MASTER = Pattern.compile("CFS: (\\d{10}); Unit: (.+?); Status: (.*?)(?: Note:[ \\.]*(.*))?");
  private static final Pattern RUN_REPORT_MASTER = Pattern.compile("(\\d{10});(.*)\\|");
  private static final Pattern DISPATCH_MASTER = Pattern.compile("([^;]+?);([^;]*?)(?:;(?:Note: *)?([^;]*))?");
  
  
  public SCClarendonCountyParser() {
    super(CITY_LIST, "CLARENDON COUNTY", "SC");
    removeWords("COURT", "LANE", "PLACE", "WAY");
    setupSaintNames("CLAIRE");
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    Matcher match = STATUS_MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("ID UNIT CALL INFO");
      data.strCallId = match.group(1);
      data.strUnit = match.group(2);
      data.strCall = match.group(3).trim();
      data.strSupp = getOptGroup(match.group(4));
      return true;
    }
    
    match = RUN_REPORT_MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = match.group(2).replace('|', '\n').trim();
      return true;
    }
    
    match = DISPATCH_MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("ADDR APT CITY PLACE CALL INFO");
      String addr = match.group(1).trim();
      String call = match.group(2).trim();
      data.strSupp = getOptGroup(match.group(3));
      
      parseAddress(StartType.START_ADDR, addr, data);
      data.strPlace = getLeft();
      
      if (call.endsWith("-")) {
        call = call.substring(0, call.length()-1).trim();
      } else if (call.startsWith("CallType Changed to ")) {
        call = call.substring(9).trim();
      } else if (call.length() == 0) {
        call = "ALERT";
      } else return false;
      data.strCall = call;
      return true;
    }
    
    return false;
  }

  static final String[] CITY_LIST = new String[]{
    "ALCOLU",
    "GABLE",
    "MANNING",
    "NEW ZION",
    "PAXVILLE",
    "SILVER",
    "SUMMERTON",
    "TURBEVILLE",
    "RIMINI",
    
    // Sumpter County
    "PINEWOOD",
    
    // Williamsburg County
    "CADES",
    "GREELEYVILLE",
    
    // Florence County
    "LAKE CITY",
    "OLANTA"
  };

}
