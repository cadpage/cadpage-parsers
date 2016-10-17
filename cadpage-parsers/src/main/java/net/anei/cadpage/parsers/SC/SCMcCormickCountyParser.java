package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;


public class SCMcCormickCountyParser extends DispatchGeoconxParser {
  
  public SCMcCormickCountyParser() {
    super("MCCORMICK COUNTY", "SC", GCX_FLG_NAME_PHONE);
  }

  @Override
  public String getFilter() {
    return "geoconex@nlamerica.com,dispatch@911email.org,dispatch@911email.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (body.startsWith("Message-Id:")) {
      int pt = body.indexOf('\n');
      if (pt < 0) return false;
      body = body.substring(pt+1).trim();
    }
    return super.parseMsg(subject, body, data);
  }
  
}
