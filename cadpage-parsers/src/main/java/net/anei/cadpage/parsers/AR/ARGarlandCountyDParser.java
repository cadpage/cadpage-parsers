package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ARGarlandCountyDParser extends DispatchA71Parser {

  public ARGarlandCountyDParser() {
    super("GARLAND COUNTY", "AR");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
