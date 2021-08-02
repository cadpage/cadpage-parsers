package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WASkagitCountyParser extends DispatchA19Parser {

  public WASkagitCountyParser() {
    super("SKAGIT COUNTY", "WA");
  }

  @Override
  public String getFilter() {
    return "@alert.active911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
