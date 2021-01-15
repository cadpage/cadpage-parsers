package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA59Parser;

public class MOScottCountyCParser extends DispatchA59Parser {

  public MOScottCountyCParser() {
    super("SCOTT COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "cpdcad@cityofchaffee.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Message")) return false;
    int pt = body.indexOf("EVENT:");
    if (pt >= 0)  body = body.substring(pt);
    return super.parseMsg("CAD", body, data);
  }
}
