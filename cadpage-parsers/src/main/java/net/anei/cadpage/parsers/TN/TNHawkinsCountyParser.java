package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class TNHawkinsCountyParser extends DispatchA71Parser {

  public TNHawkinsCountyParser() {
    super("HAWKINS COUNTY", "TN");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
