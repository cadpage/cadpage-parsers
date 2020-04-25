package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Coffee County, AL
 */

public class ALCoffeeCountyAParser extends DispatchA27Parser {
  
  public ALCoffeeCountyAParser() {
    super("COFFEE COUNTY", "AL", "[A-Z]+\\d+|[A-Z]{1,3}FD(?:TEST)?");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org,active911@enterprisepd.com";
  }
}
