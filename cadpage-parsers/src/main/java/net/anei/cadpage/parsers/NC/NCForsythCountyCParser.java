package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCForsythCountyCParser extends DispatchA71Parser {

  public NCForsythCountyCParser() {
    super("FORSYTH COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
