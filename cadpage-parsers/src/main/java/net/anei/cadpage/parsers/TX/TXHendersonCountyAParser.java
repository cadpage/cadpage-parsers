package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class TXHendersonCountyAParser extends DispatchA55Parser {

  public TXHendersonCountyAParser() {
    super("HENDERSON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "reports@messaging.eforcesoftware.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.isEmpty() && body.startsWith("dkim-signature:")) {
      subject = "Dispatch Alert";
      int pt = body.lastIndexOf("\n\nCALL DETAIL\n");
      if (pt < 0) return false;
      body = body.substring(pt+2);
    }
    return super.parseMsg(subject, body, data);
  }

}
