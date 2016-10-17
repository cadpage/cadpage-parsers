package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * Beaufort County, NC
 */
public class NCBeaufortCountyParser extends DispatchA19Parser {
  
  public NCBeaufortCountyParser() {
    super("BEAUFORT COUNTY", "NC");
  }
  
  @Override
  public String getFilter() {
    return "fireems@co.beaufort.nc.us";
  }
}
