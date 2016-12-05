package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class PAWayneCountyBParser extends DispatchA57Parser {
  
  public PAWayneCountyBParser() {
    super("WAYNE COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@wcpa911.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    if (!super.parseMsg(body,  data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " BOROUGH");
    return true;
  }

}
