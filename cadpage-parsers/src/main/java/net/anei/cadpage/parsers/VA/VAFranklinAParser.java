package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

/**
 * City of Franklin, VA
 */
public class VAFranklinAParser extends DispatchSouthernParser {
  
  private static final Pattern CALL_PATTERN = Pattern.compile("([A-Z0-9 ]+- [A-Z0-9]+) +(.*)");
  
  public VAFranklinAParser() {
    super(CITY_LIST, "FRANKLIN", "VA", 
           DSFLAG_DISPATCH_ID | DSFLAG_LEAD_PLACE | DSFLAG_CROSS_NAME_PHONE | DSFLAG_ID_OPTIONAL);
  }

  @Override
  public String getFilter() {
    return "777";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CITY OF FRANKLIN ")) return false;
    body = body.substring(17).trim();
    if (!super.parseMsg(body, data)) return false;
    if (data.strCall.length() == 0) {
      Matcher match = CALL_PATTERN.matcher(data.strSupp);
      if (match.matches()) {
        data.strCall = match.group(1);
        data.strSupp = match.group(2).trim();
      }
    }
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
    "FRANKLIN"
  };
}