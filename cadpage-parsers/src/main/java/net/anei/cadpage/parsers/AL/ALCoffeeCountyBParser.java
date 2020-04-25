package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALCoffeeCountyBParser extends DispatchA71Parser {
  
  public ALCoffeeCountyBParser() {
    super("COFFEE COUNTY", "AL");
  }
  
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
