package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class MNBrownCountyParser extends DispatchA27Parser {

  public MNBrownCountyParser() {
    super("BROWN COUNTY", "MN", "[A-Z]{3,4}");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org,cis@co.brown.mn.us";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\nNotification from CIS Active 911:");
    if (pt >= 0) body = body.substring(pt+1);
    return super.parseMsg(subject, body, data);
  }


}
