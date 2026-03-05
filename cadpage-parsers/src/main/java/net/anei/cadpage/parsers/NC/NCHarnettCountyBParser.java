package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCHarnettCountyBParser extends DispatchA71Parser {

  public NCHarnettCountyBParser() {
    super("HARNETT COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
