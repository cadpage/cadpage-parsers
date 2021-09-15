package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class INBlackfordCountyParser extends DispatchEmergitechParser {

  public INBlackfordCountyParser() {
    super(CITY_LIST, "BLACKFORD COUNTY", "IN", TrailAddrType.NONE);
  }

  private static final String[] CITY_LIST = new String[] {
      // Cities
      "HARTFORD CITY",
      "MONTPELIER",
      "DUNKIRK",

      // Towns
      "SHAMROCK LAKES",

      // Unincorporated communities
      "CONVERSE",
      "MATAMORAS",
      "MILLGROVE",
      "ROLL",
      "TRENTON"
  };
}
