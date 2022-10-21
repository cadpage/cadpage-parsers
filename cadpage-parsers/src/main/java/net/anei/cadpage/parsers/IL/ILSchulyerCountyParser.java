package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA80Parser;

public class ILSchulyerCountyParser extends DispatchA80Parser {

  public ILSchulyerCountyParser() {
    super("SCHUYLER COUNTY", "IL");
  }

  @Override
  public String getFilter() {
    return "PageGate@schuylercounty.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\nDISPATCH\n");
    if (pt >= 0) body = "DISPATCH:" + body.substring(pt+10);
    return super.parseMsg(body, data);
  }

}
