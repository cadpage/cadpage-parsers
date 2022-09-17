package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALDaleCountyAParser extends DispatchA71Parser {

  public ALDaleCountyAParser() {
    super("DALE COUNTY", "AL");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
