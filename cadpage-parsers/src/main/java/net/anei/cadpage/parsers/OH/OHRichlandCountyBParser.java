package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class OHRichlandCountyBParser extends DispatchA57Parser {

  public OHRichlandCountyBParser() {
    super("RICHLAND COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "PageGate@richlandcountyoh.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("PageGate NoReply")) return false;
    int pt = body.indexOf('\n');
    String call = body.substring(0,pt).trim();
    body = body.substring(pt+1);
    if (!body.startsWith("Call Type")) return false;
    if (!super.parseMsg(body, data)) return false;
    data.strCall = append(call, " - ", data.strCall);
    return true;
  }
}
