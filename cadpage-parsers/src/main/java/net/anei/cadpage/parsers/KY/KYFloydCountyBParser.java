package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYFloydCountyBParser extends DispatchA27Parser {

  public KYFloydCountyBParser() {
    super("FLOYD COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "prestonsburgky@cissystem.com";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n<img src=");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }
}
