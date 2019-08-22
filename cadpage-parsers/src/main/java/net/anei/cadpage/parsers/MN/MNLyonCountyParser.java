package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * LYON County, MN
 */

public class MNLyonCountyParser extends DispatchA27Parser {
  
  public MNLyonCountyParser() {
    super("LYON COUNTY", "MN", "[A-Z]+FD|\\d+[A-Z]{2}");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!body.startsWith("Notification ")) {
      int pt = body.indexOf("\n\nNotification ");
      if (pt < 0) return false;
      body = body.substring(pt+2);
    }
    return super.parseMsg(subject, body, data);
  }
  
}
