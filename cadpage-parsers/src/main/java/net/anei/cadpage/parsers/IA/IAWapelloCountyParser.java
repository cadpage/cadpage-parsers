package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.MsgInfo;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Wapello County, IA
 */

public class IAWapelloCountyParser extends DispatchA27Parser {
  
  public IAWapelloCountyParser() {
    super("WAPELLO COUNTY", "IA", "\\w+");
  }
  
  @Override
  public String getFilter() {
    return "alerts@ci.ottumwa.ia.us";
  }

  @Override
  public boolean parseMsg(String subject, String body, MsgInfo.Data data) {
    int pt = body.indexOf("\n\nThis message and");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }
}
