package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class PASomersetCountyBParser extends DispatchSPKParser {
  
  public PASomersetCountyBParser() {
    super("SOMERSET COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "spamsuxs@gmail.com";
  }
 
}
