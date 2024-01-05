package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class KYHendersonCountyParser extends DispatchA1Parser {

  public KYHendersonCountyParser() {
    super("HENDERSON COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "E911@cityofhendersonky.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("AUTOMATED MESSAGE DO NOT REPLY")) {
      if (!body.startsWith("AUTOMATED MESSAGE DO NOT REPLY\n")) return false;
      body = body.substring(31);
    }
    int pt = body.indexOf('\n');
    if (pt < 0) return false;
    subject = body.substring(0, pt).trim();
    body = body.substring(pt+1).trim();
    return super.parseMsg(subject, body, data);
  }
}
