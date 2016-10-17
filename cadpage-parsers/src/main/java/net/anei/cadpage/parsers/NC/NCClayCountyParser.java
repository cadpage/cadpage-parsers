package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;

/**
 * Clay County, NC
 */
public class NCClayCountyParser extends DispatchGeoconxParser {
  
  public NCClayCountyParser() {
    super("CLAY COUNTY", "NC");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@911email.net,todd.beasley@geoconex.com";
  }
}
