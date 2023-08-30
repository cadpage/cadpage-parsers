package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCMartinCountyCParser extends DispatchA71Parser {

  public NCMartinCountyCParser() {
    super("MARTIN COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
