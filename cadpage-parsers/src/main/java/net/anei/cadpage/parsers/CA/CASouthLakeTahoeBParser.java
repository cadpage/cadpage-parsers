package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

public class CASouthLakeTahoeBParser extends DispatchA20Parser {

  public CASouthLakeTahoeBParser() {
    super("SOUTH LAKE TAHOE", "CA");
  }

  @Override
  public String getFilter() {
    return "rimstxt@cityofslt.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\nThis is a verified");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }

}
