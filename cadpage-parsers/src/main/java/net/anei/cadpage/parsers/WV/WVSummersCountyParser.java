package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchA46Parser;

public class WVSummersCountyParser extends DispatchA46Parser {

  public WVSummersCountyParser() {
    super(CITY_LIST, "SUMMERS COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "summerscounty911@pagingpts.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final String[] CITY_LIST = new String[] {

      // City
      "HINTON",

      // Magisterial districts
      "BLUESTONE RIVER",
      "FOREST HILL",
      "GREEN SULPHUR",
      "GREENBRIER",
      "GREENBRIER RIVER",
      "JUMPING BRANCH",
      "NEW RIVER",
      "PIPESTEM",
      "TALCOTT",

      // Other places
      "BROOKS",
      "BUCK",
      "ELTON",
      "FOREST HILL",
      "GREEN SULPHUR SPRINGS",
      "HIX",
      "INDIAN MILLS",
      "JUMPING BRANCH",
      "LOWELL",
      "MARIE",
      "NIMITZ",
      "PENCE SPRINGS",
      "PIPESTEM",
      "SANDSTONE",
      "TALCOTT",

      //  Greenbriar County
      "ALDERSON"
  };

}
