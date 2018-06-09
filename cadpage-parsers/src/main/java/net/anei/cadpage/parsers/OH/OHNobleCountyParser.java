package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class OHNobleCountyParser extends DispatchA1Parser {
  
  public OHNobleCountyParser() {
    super("NOBLE COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "@noblecountysheriffsoffice.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) {
      if (body.startsWith("Alert:")) {
        int pt = body.indexOf('\n');
        String tmp = body.substring(pt+1).trim();
        if (tmp.startsWith("ALRM LVL:")) {
          subject = body.substring(0,pt).trim();
          body = tmp;
        }
      }
      else if (body.startsWith("ALRM LVL:")) subject = "Alert:";
    }
    return super.parseMsg(subject, body, data);
  }
  
}
