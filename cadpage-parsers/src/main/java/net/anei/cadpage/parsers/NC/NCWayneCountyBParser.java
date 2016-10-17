package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA13Parser;


public class NCWayneCountyBParser extends DispatchA13Parser {
  
  public NCWayneCountyBParser() {
    super("WAYNE COUNTY", "NC");
  }
  
  @Override
  public String getFilter() {
    return "waynecounty911@waynegov.com";
  }
}
