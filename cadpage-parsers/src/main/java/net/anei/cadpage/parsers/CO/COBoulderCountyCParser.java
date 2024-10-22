package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA97Parser;

public class COBoulderCountyCParser extends DispatchA97Parser {

  public COBoulderCountyCParser() {
    super("BOULDER COUNTY", "CO");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
