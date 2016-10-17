package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class MODallasCountyParser extends DispatchA25Parser {
  
  public MODallasCountyParser() {
    super("DALLAS COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "Alerts@dallascomo911.us";
  }
}
