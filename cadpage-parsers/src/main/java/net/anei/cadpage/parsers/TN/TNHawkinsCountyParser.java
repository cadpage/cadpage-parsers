package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNHawkinsCountyParser extends DispatchA74Parser {

  public TNHawkinsCountyParser() {
    super("HAWKINS COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@HawkinsTN911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
