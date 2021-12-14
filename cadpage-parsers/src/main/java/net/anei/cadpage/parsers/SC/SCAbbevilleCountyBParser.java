package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class SCAbbevilleCountyBParser extends DispatchA71Parser {

  public SCAbbevilleCountyBParser() {
    super("ABBEVILLE COUNTY", "SC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
