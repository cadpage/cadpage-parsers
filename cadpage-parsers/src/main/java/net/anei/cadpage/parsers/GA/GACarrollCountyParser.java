package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class GACarrollCountyParser extends SmartAddressParser {
  
  private static final Pattern MASTER = Pattern.compile("Carroll911:(?:-([A-Z0-9]+)-)?(.*?) Location: (.*?) //(?: (.*))?");
  
  public GACarrollCountyParser() {
    super(CITY_LIST, "CARROLL COUNTY", "GA");
    setFieldList("CODE CALL ADDR APT CITY INFO");
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCode = getOptGroup(match.group(1));
    data.strCall = match.group(2);
    parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, match.group(3).trim(), data);
    data.strSupp = getOptGroup(match.group(4));
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
    "BOWDON",
    "BREMEN",
    "CARROLLTON",
    "MOUNT ZION",
    "ROOPVILLE",
    "TEMPLE",
    "VILLA RICA",
    "WHITESBURG",
    
    // Douglas County
    "WINSTON",
    
    // Haralson County
    "WACO"
  };
}
