package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA79Parser;

public class VADanvilleBParser extends DispatchA79Parser {

  public VADanvilleBParser() {
    super("Trip", "DANVILLE", "VA");
  }

  @Override
  public String getFilter() {
    return "ServerAlerts@dlsc.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\nCONFIDENTIALITY NOTICE:");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }
}
