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
    int pt = body.indexOf(", RUN CARD:");
    if (pt >= 0) {
      subject = body.substring(0, pt).trim();
      body = body.substring(pt+2);
      body = body.replace("\n ", " ");
    }
    return super.parseMsg(subject, body, data);
  }
  
}
