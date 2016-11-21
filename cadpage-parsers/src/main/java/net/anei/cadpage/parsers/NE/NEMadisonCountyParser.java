package net.anei.cadpage.parsers.NE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class NEMadisonCountyParser extends SmartAddressParser {

  public NEMadisonCountyParser() {
    super("MADISON COUNTY", "NE");
    setFieldList("SRC CALL PLACE ADDR APT INFO");
  }
  
  @Override
  public String getFilter() {
    return "@c-msg.net";
  }
  
  private static final Pattern PREFIX = Pattern.compile("((?:.*? )?(?:FIRE AND RESCUE|FIRE|RESCUE)) (?:NEEDED|REQUESTED)(?: (?:TO|AT))? +");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.endsWith(" Dispatch")) return false;
    data.strSource = subject.substring(0,subject.length()-9).trim();
    Matcher match = PREFIX.matcher(body);
    if (match.lookingAt()) {
      data.strCall = match.group(1);
      body = body.substring(match.end());
      if (body.startsWith("FOR ")) {
        body = body.substring(4).trim();
        String call = data.strCall;
        data.strCall = "";
        parseAddress(StartType.START_CALL, body, data);
        data.strCall = append(call, " - ", data.strCall);
      } else {
        parseAddress(StartType.START_ADDR, body, data);
      }
      data.strSupp = getLeft();
      return true;
    }
    
    parseAddress(StartType.START_CALL, body, data);
    data.strSupp = getLeft();
    return true;
  }
}
