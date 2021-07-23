package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

/**
 * Allen County, KY
 */
public class KYAllenCountyParser extends DispatchA65Parser {

  public KYAllenCountyParser() {
    super(CITY_LIST, "ALLEN COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm1.info,@allenkye911.info";
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
