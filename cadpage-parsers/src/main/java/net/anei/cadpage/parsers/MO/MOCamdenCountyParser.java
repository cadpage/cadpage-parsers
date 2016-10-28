package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class MOCamdenCountyParser extends DispatchSPKParser {
  public MOCamdenCountyParser() {
    super("CAMDEN COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "dispatch@camdenmo.org";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    int pt = data.strSupp.indexOf("CONFIDENTIALITY NOTICE:");
    if (pt >= 0) data.strSupp = data.strSupp.substring(0, pt).trim();
    return true;
  }
  
}
