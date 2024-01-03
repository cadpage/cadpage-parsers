package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA90Parser;

public class NCWayneCountyEParser extends DispatchA90Parser {

  public NCWayneCountyEParser() {
    super("WAYNE COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
