package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * Bourbon County, KY
 */
public class KYBourbonCountyBParser extends DispatchA19Parser {
  
  @Override
  public String getFilter() {
    return "@alert.active911.com";
  }
  
  public KYBourbonCountyBParser() {
    super("BOURBON COUNTY", "KY");
  }
  
}
