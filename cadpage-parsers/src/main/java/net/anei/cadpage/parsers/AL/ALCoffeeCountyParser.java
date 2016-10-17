package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Coffee County, AL
 */

public class ALCoffeeCountyParser extends DispatchA27Parser {
  
  public ALCoffeeCountyParser() {
    super("COFFEE COUNTY", "AL", "[A-Z]+\\d+|[A-Z]{1,3}FD(?:TEST)?");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
