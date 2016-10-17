package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Peppin County, WI
 */

public class WIPeppinCountyParser extends DispatchA27Parser {
  
  public WIPeppinCountyParser() {
    super("PEPPIN COUNTY", "WI", "[A-Z]+\\d+|[A-Z]*(?:EMS|FR)");
  }
  
  @Override
  public String getFilter() {
    return "pepinamb@pepinwisconsin.org,cisincident@co.pepin.wi.us";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\nConfidentiality Notice:");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }
  
}
