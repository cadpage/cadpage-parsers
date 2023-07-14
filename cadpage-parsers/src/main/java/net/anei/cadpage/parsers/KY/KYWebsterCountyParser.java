package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYWebsterCountyParser extends DispatchA74Parser {

  public KYWebsterCountyParser() {
    super(CITY_LIST, "WEBSTER COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "Dispatch@Providence911.info,dispatch@webster911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CLAY",
      "DIXON",
      "PROVIDENCE",
      "SEBREE",
      "SLAUGHTERS",
      "WHEATCROFT",

      // Census-designated places
      "ONTON",
      "POOLE",

      // Other unincorporated communities
      "BLACKFORD",
      "DERBY",
      "DIAMOND",
      "ELMWOOD",
      "FAIRMONT",
      "FREE UNION",
      "HEARIN",
      "JOLLY",
      "LISMAN",
      "LITTLE ZION",
      "ORTIZ",
      "PRATT",
      "STANHOPE",
      "VANDERBURG",
      "WANAMAKER",

      // Hopkins County
      "MADISONVILLE"

  };

}
