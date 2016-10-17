package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

/**
 * Summit County, OH
 */
public class OHSummitCountyDParser extends SmartAddressParser {
  
  public OHSummitCountyDParser() {
    super("SUMMIT COUNTY", "OH");
    setFieldList("CALL ID DATE TIME ADDR APT UNIT INFO");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@police.kent.edu";
  }
  
  private static final Pattern MASTER = Pattern.compile("(.*?) (?:(\\d{4}-\\d{8}) )?(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d) \\d\\d:\\d\\d (.*)");
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    data.strCallId = getOptGroup(match.group(2));
    data.strDate = match.group(3);
    data.strTime = match.group(4);
    
    for (String part : MBLANK_PTN.split(match.group(5))) {
      if (data.strAddress.length() == 0) {
        parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT, part, data);
      } else if (part.startsWith("Dispatch received by unit ")) {
        data.strUnit = append(data.strUnit, " ", part.substring(26).trim());
      } else {
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
    return true;
  }
}