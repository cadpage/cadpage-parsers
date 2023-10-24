package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.dispatch.DispatchA94Parser;

public class WIBrownCountyCParser extends DispatchA94Parser {

  public WIBrownCountyCParser() {
    super("BROWN COUNTY", "WI");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
