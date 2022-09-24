package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCNorthamptonCountyBParser extends DispatchA71Parser {

  public NCNorthamptonCountyBParser() {
    super("NORTHAMPTON COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
