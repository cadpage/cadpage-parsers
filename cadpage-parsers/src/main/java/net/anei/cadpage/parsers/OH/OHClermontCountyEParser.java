package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class OHClermontCountyEParser extends DispatchH03Parser {

  public OHClermontCountyEParser() {
    super("CLERMONT COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "13COMM@ohio.gov";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    int pt = data.strCity.indexOf('/');
    if (pt >= 0) data.strCity = data.strCity.substring(pt+1).trim();
    return true;
  }

}
