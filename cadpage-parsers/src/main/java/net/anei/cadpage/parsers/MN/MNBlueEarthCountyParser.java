package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Blue Earth County, MN
 */
public class MNBlueEarthCountyParser extends DispatchA27Parser {
  
  public MNBlueEarthCountyParser() {
    super("BLUE EARTH COUNTY", "MN", "[A-Z]+\\d+[A-Z]?|[A-Z]{1,3}FD");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
  