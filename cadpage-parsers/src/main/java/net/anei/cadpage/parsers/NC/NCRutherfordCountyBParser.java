package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCRutherfordCountyBParser extends DispatchA71Parser {

  public NCRutherfordCountyBParser() {
    super("RUTHERFORD COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return  MAP_FLG_PREFER_GPS;
  }
}
