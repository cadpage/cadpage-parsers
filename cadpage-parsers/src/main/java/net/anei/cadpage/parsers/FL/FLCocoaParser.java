package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class FLCocoaParser extends SmartAddressParser {

  public FLCocoaParser() {
    super("COCOA", "FL");
    setFieldList("UNIT MAP ADDR APT PLACE CITY CALL ID");
  }
  
  @Override
  public String getFilter() {
    return "DispatchDoNotReply@cocoafl.org";
  }
  
  private static final Pattern MASTER = Pattern.compile("(?:([ A-Z0-9]+?)  )?(?:Area (\\S+) )?(.+)(COCOA|MERRIT ISLAND|ROCKLEDGE) (.+?)(?: (\\d{4}-\\d{8}))?");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Information")) return false;
    
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strUnit = getOptGroup(match.group(1));
    data.strMap = getOptGroup(match.group(2));
    parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT, match.group(3).trim(), data);
    data.strPlace = getLeft();
    data.strCity = match.group(4);
    data.strCall = match.group(5).trim();
    data.strCallId = getOptGroup(match.group(6));
    
    // Two many optional fields.  Make sure we have at least one
    return data.strUnit.length() > 0 ||
           data.strMap.length() > 0 ||
           data.strCallId.length() > 0;
  }
}
