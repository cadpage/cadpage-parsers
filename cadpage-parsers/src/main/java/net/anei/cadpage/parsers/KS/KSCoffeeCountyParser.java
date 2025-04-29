package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class KSCoffeeCountyParser extends DispatchA19Parser {

  public KSCoffeeCountyParser() {
    super("COFFEE COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "active911@coffeycountyks.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
