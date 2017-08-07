package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA51Parser;


public class CALosAngelesCountyCParser extends DispatchA51Parser {
  
  public CALosAngelesCountyCParser() {
    super("LOS ANGELES COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "No-reply@areaefire.org";
  }
}
