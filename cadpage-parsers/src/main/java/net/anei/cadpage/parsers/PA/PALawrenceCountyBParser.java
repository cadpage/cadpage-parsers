package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class PALawrenceCountyBParser extends DispatchH03Parser {
  
  public PALawrenceCountyBParser() {
    super("LAWRENCE COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "C@leoc.net,@RCAD911.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " BORO");
    return true;
  }

}
