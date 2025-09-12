package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOCaldwellCountyAParser extends DispatchBCParser {
  
  public MOCaldwellCountyAParser() {
    super("CALDWELL COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@OMNIGO.COM";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    
    if (body.startsWith("WARNING:")) {
      int pt = body.indexOf("\n\n");
      if (pt < 0) return false;
      body = body.substring(pt+2).trim();
    }
    
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    
    // Subject usually has a more informative call description.
    // then what is in the alert text
    if (subject.length() > 0) {
      if (subject.startsWith("STEALING INVESTIGATION")) subject = "STEALING INVESTIGATION";
      data.strCall = subject;
    }
    return true;
  }
  
}
