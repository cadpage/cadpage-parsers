package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchC01Parser;

public class MIKentCountyBParser extends DispatchC01Parser {

  public MIKentCountyBParser() {
    super("KENT COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "KCCCAlert@kent911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
