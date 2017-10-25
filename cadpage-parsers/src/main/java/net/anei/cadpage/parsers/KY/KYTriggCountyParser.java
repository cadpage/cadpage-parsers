package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;

/**
 * Trigg County, KY
 */
public class KYTriggCountyParser extends DispatchGeoconxParser {
  
  public KYTriggCountyParser() {
    super("TRIGG COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@911comm2.info,dispatch@911email.org";
  }
}
