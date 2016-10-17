package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class NYTiogaCountyParser extends SmartAddressParser {
  
  public NYTiogaCountyParser() {
    super(CITY_LIST, "TIOGA COUNTY", "NY");
    setFieldList("ID CALL ADDR APT CITY DATE TIME INFO");
  }
  
  @Override
  public String getFilter() {
    return "impactpaging@co.tioga.ny.us";
  }
  
  private static final Pattern MASTER = Pattern.compile("([A-Z]{2}-\\d{6}-\\d\\d) (.*?)  (\\d\\d/\\d\\d/\\d{4}) (\\d{2})(\\d{2})(?:  +(.*))?");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    String addr = match.group(2).trim();
    data.strDate = match.group(3);
    data.strTime = match.group(4) + ':' + match.group(5);
    data.strSupp = getOptGroup(match.group(6));
    
    StartType st = StartType.START_CALL;
    int flags = FLAG_START_FLD_REQ;
    int pt = addr.lastIndexOf("  ");
    if (pt >= 0) {
      st = StartType.START_ADDR;
      flags = 0;
      data.strCall = addr.substring(0,pt).trim();
      addr = addr.substring(pt+2).trim();
    }
    parseAddress(st, flags | FLAG_ANCHOR_END, addr, data);
    data.strCity = convertCodes(data.strCity.toUpperCase(), MISSPELLED_CITY_TABLE);
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Towns
    "BARTON",
    "BERKSHIRE",
    "CANDOR",
    "NEWARK VALLEY",
    "NICHOLS",
    "OWEGO",
    "RICHFORD",
    "SPENCER",
    "TIOGA",

    // Villages
    "CANDOR VILLAGE",
    "NEWARK VALLEY VILLAGE",
    "NICHOLS VILLAGE",
    "OWEGO VILLAGE",
    "SPENCER VILLAGE",
    "WAVERLY VILLAGE",

    // Census-designated place
    "APALACHIN",

    // Hamlet
    "LOUNSBERRY",
    
    // Broome County
    "MAINE",
    "SMAINE"
  };
  
  private static final Properties MISSPELLED_CITY_TABLE = buildCodeTable(new String[]{
      "SMAINE",     "MAINE"
  });
}
