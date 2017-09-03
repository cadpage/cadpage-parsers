package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyLParser extends PAChesterCountyBaseParser {
  
  public PAChesterCountyLParser() {
    super(null);
    setFieldList("TIME CALL ADDR APT CITY PLACE DATE ID CITY X");
  }
  
  private static final Pattern MASTER = 
      Pattern.compile("(\\d\\d:\\d\\d)  +([^\\*]+?)(?: \\*)?  ([^,]+?(?:,[ A-Z0-9]+?)?)  (.+)  (\\d\\d/\\d\\d/\\d\\d)  ([A-Z]{1,4}\\d{8})  +([A-Z]+)(?:  +(.*))?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Trappe")) return false;
    
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strTime = match.group(1);
    data.strCall = match.group(2).trim();
    parseChesterAddress(match.group(3).trim(), data);
    data.strPlace = stripFieldStart(match.group(4).trim(), "-");
    data.strDate = match.group(5);
    data.strCallId = match.group(6);
    data.strCity = convertCityCode(match.group(7));
    data.strCross = getOptGroup(match.group(8));

    return true;
  }
} 
