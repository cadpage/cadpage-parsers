package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchA98Parser;

public class SCEdgefieldCountyParser extends DispatchA98Parser {

  public SCEdgefieldCountyParser() {
    super("EDGEFIELD COUNTY", "SC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
