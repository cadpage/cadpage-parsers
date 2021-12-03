package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;


public class TNWhiteCountyParser extends DispatchA74Parser {

  public TNWhiteCountyParser() {
    super(CITY_LIST, "WHITE COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@whitecoe911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final String[] CITY_LIST = new String[]{

      "BON AIR",
      "DOYLE",
      "CASSVILLE",
      "COOKEVILLE",
      "DEROSSETT",
      "QUEBECK",
      "RAVENSCROFT",
      "SPARTA",
      "SPENCER",
      "WALLING",
      "YANKEETOWN"

  };
}
