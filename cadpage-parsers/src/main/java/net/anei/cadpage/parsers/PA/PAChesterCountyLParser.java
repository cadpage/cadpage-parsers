package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyLParser extends PAChesterCountyBaseParser {
  
  private static final Pattern MASTER = 
      Pattern.compile("(\\d\\d:\\d\\d)  +([^\\*]+?)(?: \\*)?  ([^,]+?(?:,[ A-Z0-9]+?)?)  (.+)  (\\d\\d/\\d\\d/\\d\\d)  ([A-Z]{4}\\d{8})\\b(.*)");
  private static final Pattern SRC_PTN = Pattern.compile("  +(?:[A-Z]+  +)?(\\d+)\\b");
  private static final Pattern UNIT_PTN = Pattern.compile("  +([A-Z0-9]+),$");
  
  public PAChesterCountyLParser() {
    super(null);
    setFieldList("TIME CALL ADDR APT CITY PLACE DATE ID SRC X UNIT");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Trappe")) return false;
    
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strTime = match.group(1);
    data.strCall = match.group(2).trim();
    parseChesterAddress(match.group(3).trim(), data);
    data.strPlace = stripFieldEnd(match.group(4).trim(), "-");
    data.strDate = match.group(5);
    data.strCallId = match.group(6);
    String left = match.group(7);

    match = SRC_PTN.matcher(left);
    if (match.lookingAt()) {
      data.strSource = match.group(1);
      left = left.substring(match.end());
    }
    
    match = UNIT_PTN.matcher(left);
    if (match.find()) {
      data.strUnit = match.group(1);
      left = left.substring(0,match.start());
    }

    data.strCross = left.trim();
    return true;
  }
} 
