package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class TXHendersonCountyAParser extends DispatchA64Parser {

  public TXHendersonCountyAParser() {
    super("HENDERSON COUNTY", "TX");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (body.startsWith("dkim-signature:")) {
      if (!body.contains("\nSubject:   Dispatch Alert")) return false;
      body = body.replace("=\n", "");
      int pt1 = body.indexOf("\n  Call Type:");
      if (pt1 < 0) return false;
      int pt2 = body.indexOf('\n', pt1+13);
      if (pt2 < 0) pt2 = body.length();
      subject = "Dispatch Alert";
      body = body.substring(pt1+1, pt2).trim();
      body = stripFieldEnd(body, "=20");
    }

    return super.parseMsg(subject, body, data);
  }

  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com";
  }

}
