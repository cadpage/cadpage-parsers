package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

/**
 * Allen County, KY
 */
public class KYAllenCountyParser extends DispatchA74Parser {

  public KYAllenCountyParser() {
    super(CITY_LIST, "ALLEN COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@AllenKYE911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final String[] CITY_LIST = new String[]{
      "ADOLPHUS",
      "AMOS",
      "HALIFAX",
      "HALFWAY",
      "HOLLAND",
      "MEADOR",
      "NEW ROE",
      "PETROLEUM",
      "SCOTTSVILLE"
  };
}
