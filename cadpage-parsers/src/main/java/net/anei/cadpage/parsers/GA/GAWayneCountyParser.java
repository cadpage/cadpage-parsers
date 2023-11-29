package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class GAWayneCountyParser extends DispatchSPKParser {
  
  public GAWayneCountyParser() {
    super("WAYNE COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@waynecounty-ga.gov";
  }

}
