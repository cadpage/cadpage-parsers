package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;


public class KYCallowayCountyParser extends DispatchA27Parser {
  
  public KYCallowayCountyParser() {
    super("CALLOWAY COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
