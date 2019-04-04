package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;



public class PAMonroevilleParser extends DispatchA1Parser {
  
  public PAMonroevilleParser() {
    super("MONROEVILLE", "PA");
  }
  
  @Override
  public String getFilter() {
    return "alertts@monroeville.pa.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("AUTOMATED MESSAGE DO NOT REPLY")) {
      int pt = body.indexOf(", RUN CARD:");
      if (pt < 0) return false;
      subject = body.substring(0, pt).trim();
      body = body.substring(pt+2);
    }
    body = body.replace("\n ", " ");
    return super.parseMsg(subject, body, data);
  }
  
}
