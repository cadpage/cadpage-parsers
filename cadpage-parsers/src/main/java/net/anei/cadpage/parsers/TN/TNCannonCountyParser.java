package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNCannonCountyParser extends DispatchA74Parser {

  public TNCannonCountyParser() {
    super("CANNON COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@cannontne911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
