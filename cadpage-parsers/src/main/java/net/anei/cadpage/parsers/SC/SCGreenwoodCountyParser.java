package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class SCGreenwoodCountyParser extends DispatchSPKParser {
  
  public SCGreenwoodCountyParser() {
    super("GREENWOOD COUNTY", "SC");
  }
  
  @Override
  public String getFilter() {
    return "greenwood.county@greenwoodsc.gov";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    int pt = body.indexOf("NOTE:");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseHtmlMsg(subject, body, data);
  }
  
}
