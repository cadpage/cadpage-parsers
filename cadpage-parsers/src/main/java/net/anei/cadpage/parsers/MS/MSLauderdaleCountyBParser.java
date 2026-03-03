package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class MSLauderdaleCountyBParser extends DispatchA71Parser {

  public MSLauderdaleCountyBParser() {
    super("LAUDERDALE COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "no-reply@angeltracksoftware.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\n--\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }

}
