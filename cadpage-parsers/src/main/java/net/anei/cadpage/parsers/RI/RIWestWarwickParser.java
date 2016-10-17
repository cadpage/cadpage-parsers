package net.anei.cadpage.parsers.RI;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class RIWestWarwickParser extends MsgParser {
 
  public RIWestWarwickParser() {
    super("WEST WARWICK", "RI");
    setFieldList("CALL ADDR APT INFO");
  }
  
  @Override
  public String getFilter() {
    return "wwfddispatch@westwarwickri.org";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    // Too generic.  Do not accept unless positively identified
    if (subject.length() == 0 || !isPositiveId()) return false;
    data.strCall = subject;
    int pt = body.indexOf('\n');
    if (pt >= 0) {
      data.strSupp = body.substring(pt+1).trim();
      body = body.substring(0,pt).trim();
    }
    parseAddress(body, data);
    return true;
  }
}
