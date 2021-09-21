package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA83Parser;

public class GAWashingtonCountyBParser extends DispatchA83Parser {

  public GAWashingtonCountyBParser() {
    super(CITY_LIST, "WASHINGTON COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "washington.ga@ez911map.net,CAD@ssialerts.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final String[] CITY_LIST = new String[]{
      "DAVISBORO",
      "DEEPSTEP",
      "HARRISON",
      "OCONEE",
      "RIDDLEVILLE",
      "SANDERSVILLE",
      "TENNILLE",
      "WARTHEN"
  };
}
