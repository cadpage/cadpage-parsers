package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALChoctawCountyBParser extends DispatchA71Parser {

  public ALChoctawCountyBParser() {
    super("CHOCTAW COUNTY", "AL");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
