package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class PALawrenceCountyBParser extends DispatchH03Parser {
  
  public PALawrenceCountyBParser() {
    super("LAWRENCE COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "37114.37C@leoc.net";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " BORO");
    return true;
  }

}
