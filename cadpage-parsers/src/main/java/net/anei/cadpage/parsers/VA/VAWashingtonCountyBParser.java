package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class VAWashingtonCountyBParser extends DispatchA71Parser {

  public VAWashingtonCountyBParser() {
    super("WASHINGTON COUNTY", "VA");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
