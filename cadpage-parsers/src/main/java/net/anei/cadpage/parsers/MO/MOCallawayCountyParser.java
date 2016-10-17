package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;



public class MOCallawayCountyParser extends DispatchA25Parser {
 
  public MOCallawayCountyParser() {
    super("CALLAWAY COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "EnterpolAlerts@cceoc.org";
  }
}