package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.dispatch.DispatchA46Parser;

public class ARGarlandCountyCParser extends DispatchA46Parser {

  public ARGarlandCountyCParser() {
    super(CITY_LIST, "GARLAND COUNTY", "AR");
  }

  @Override
  public String getFilter() {
    return "hotsprings@pagingpts.com,hotspringsvillage@ptspaging.com";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "HOT SPRINGS",

      // Towns
      "FOUNTAIN LAKE",
      "LONSDALE",
      "MOUNTAIN PINE",

      // Census-designated places
      "HOT SPRINGS",
      "HOT SPRINGS VILLAGE",
      "LAKE HAMILTON",
      "PINEY",
      "ROCKWELL",

      // Unincorporated communities
      "JESSIEVILLE",
      "BEAR",
      "ROYAL"
  };
}
