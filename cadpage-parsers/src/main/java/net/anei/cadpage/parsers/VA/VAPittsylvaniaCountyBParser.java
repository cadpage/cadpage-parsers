package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchC10Parser;

public class VAPittsylvaniaCountyBParser extends DispatchC10Parser {

  public VAPittsylvaniaCountyBParser() {
    super("PITTSYLVANIA COUNTY", "VA");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
