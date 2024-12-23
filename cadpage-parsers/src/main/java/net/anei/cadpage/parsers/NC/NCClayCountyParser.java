package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

/**
 * Clay County, NC
 */
public class NCClayCountyParser extends DispatchSPKParser {

  public NCClayCountyParser() {
    super("CLAY COUNTY", "NC");
  }

  @Override
  public String getFilter() {
    return "claync911cad@claync.us";
  }
}
