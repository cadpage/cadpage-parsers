package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;



public class WAStevensCountyParser extends DispatchA19Parser {

  public WAStevensCountyParser() {
    super("STEVENS COUNTY", "WA");
  }

  @Override
  public String getFilter() {
    return "spillmancad.gov,@lifeflight.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
