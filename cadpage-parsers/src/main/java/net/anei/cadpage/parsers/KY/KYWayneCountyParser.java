package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYWayneCountyParser extends DispatchA74Parser {

  public KYWayneCountyParser() {
    super("WAYNE COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@WayneKYe911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
