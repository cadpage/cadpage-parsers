package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;

/**
 * Allen County, KY
 */
public class KYAllenCountyParser extends DispatchGeoconxParser {
  
  public KYAllenCountyParser() {
    super("ALLEN COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "allencoky911@911email.net";
  }
}
