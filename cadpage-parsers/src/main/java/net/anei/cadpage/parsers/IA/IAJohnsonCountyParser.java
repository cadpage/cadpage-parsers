package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchA38Parser;

public class IAJohnsonCountyParser extends DispatchA38Parser {
  
 
  public IAJohnsonCountyParser() {
    super("JOHNSON COUNTY", "IA");
  }
  
  @Override
  public String getFilter() {
    return "noreply@jecc-ema.org";
  }
  
}
