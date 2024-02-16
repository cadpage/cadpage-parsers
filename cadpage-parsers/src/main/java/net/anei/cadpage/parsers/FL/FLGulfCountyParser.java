package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class FLGulfCountyParser extends DispatchA71Parser {

  public FLGulfCountyParser() {
    super("GULF COUNTY", "FL");
  }

  @Override
  public String getFilter() {
    return "Support@beaconss.com";
  }
}
