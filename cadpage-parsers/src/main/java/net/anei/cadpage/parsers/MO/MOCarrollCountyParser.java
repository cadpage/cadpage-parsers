package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class MOCarrollCountyParser extends DispatchA25Parser {
  
  public MOCarrollCountyParser() {
    super("CARROLL COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "alerts@carollcomo911.org";
  }
}
