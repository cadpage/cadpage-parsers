package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

public class MIIsabellaCountyBParser extends DispatchA20Parser {
  
  public MIIsabellaCountyBParser() {
    super("ISABELLA COUNTY", "MI");
  }
  
  @Override
  public String getFilter() {
    return "@sagchip.org";
  }
 
}
