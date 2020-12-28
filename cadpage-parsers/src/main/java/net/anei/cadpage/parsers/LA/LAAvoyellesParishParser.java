package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.dispatch.DispatchA46Parser;

public class LAAvoyellesParishParser extends DispatchA46Parser {

  public LAAvoyellesParishParser() {
    super(CITY_LIST, "AVOYELLES PARISH", "LA");
  }

  @Override
  public String getFilter() {
    return "PTS@ptssolutions.com";
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "BUNKIE",
      "MARKSVILLE",

      // Towns
      "COTTONPORT",
      "EVERGREEN",
      "MANSURA",
      "SIMMESPORT",

      // Villages
      "HESSMER",
      "MOREAUVILLE",
      "PLAUCHEVILLE",

      // DMA
      "Alexandria",

      // Census-designated places
      "BORDELONVILLE",
      "CENTER POINT",
      "FIFTH WARD"
  };
}
