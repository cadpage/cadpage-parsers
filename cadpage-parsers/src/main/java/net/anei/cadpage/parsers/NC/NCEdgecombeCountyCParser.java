package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCEdgecombeCountyCParser extends DispatchA71Parser {

  public NCEdgecombeCountyCParser() {
    super("EDGECOMBE COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
