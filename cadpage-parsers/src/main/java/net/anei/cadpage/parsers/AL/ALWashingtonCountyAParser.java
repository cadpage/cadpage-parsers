package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALWashingtonCountyAParser extends DispatchA71Parser {

  public ALWashingtonCountyAParser() {
    super("WASHINGTON COUNTY", "AL");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
