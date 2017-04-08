package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class SCClarendonCountyParser extends SmartAddressParser {
  
  private static final Pattern DISPATCH_MASTER = Pattern.compile("(.*) (\\d\\d:\\d\\d:\\d\\d) (.*)");
  private static final Pattern STATUS_MASTER = Pattern.compile("CFS: (\\d{10}) Unit: (.+) Status: (.*?)(?: Note:[ \\.]*(.*))?");
  
  public SCClarendonCountyParser() {
    super(CITY_LIST, "CLARENDON COUNTY", "SC");
    setupMultiWordStreets(
        "ANN WORSHAM",
        "JACKS CREEK",
        "MILL CREEK",
        "MW RICKENBAKER",
        "PLOWDEN MILL",
        "SAM NEXSEN",
        "SNOWY RIVER",
        "WA GAMBLE"
    );
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = DISPATCH_MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("ADDR APT CITY TIME CALL INFO");
      String addr = match.group(1).trim();
      data.strTime = match.group(2);
      data.strCall = match.group(3).trim();
      
      parseAddress(StartType.START_PLACE, FLAG_ANCHOR_END, addr, data);
      if (data.strAddress.length() == 0) {
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, data.strPlace, data);
        data.strPlace = "";
      }
      if (data.strAddress.contains("&")) {
        data.strAddress = stripFieldStart(data.strAddress, "1 ");
        return true;
      }
      return data.strCity.length() > 0;
    }
    
    match = STATUS_MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("ID UNIT CALL INFO");
      data.strCallId = match.group(1);
      data.strUnit = match.group(2);
      data.strCall = match.group(3).trim();
      data.strSupp = getOptGroup(match.group(4));
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
    "GREENLEYVILLE",
    
    // Florence County
    "LAKE CITY",
    "OLANTA"
  };

}
