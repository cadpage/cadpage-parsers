package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCTransylvaniaCountyParser extends DispatchA71Parser {

  public NCTransylvaniaCountyParser() {
    super("TRANSYLVANIA COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
