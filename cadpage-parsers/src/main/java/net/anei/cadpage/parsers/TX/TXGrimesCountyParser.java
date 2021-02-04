package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;

public class TXGrimesCountyParser extends DispatchA18Parser {

  public TXGrimesCountyParser() {
    super(CITY_LIST, "GRIMES COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "Fire@GrimesCountyTexas.gov";
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "ANDERSON",
      "BEDIAS",
      "IOLA",
      "NAVASOTA",
      "PLANTERSVILLE",
      "TODD MISSION",

      // Unincorporated communities
      "APOLONIA",
      "RICHARDS",
      "ROANS PRAIRIE",
      "SHIRO",
      "STONEHAM"
  };
}
