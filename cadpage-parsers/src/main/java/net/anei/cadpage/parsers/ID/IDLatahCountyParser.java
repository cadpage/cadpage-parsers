package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class IDLatahCountyParser extends DispatchA19Parser {

  public IDLatahCountyParser() {
    super("LATAH COUNTY", "ID");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com,@email.getrave.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\nClick the following link");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }

}
