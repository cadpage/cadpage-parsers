package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchLifeNetEMSParser;

public class TXRockwallCountyFParser extends DispatchLifeNetEMSParser {

  public TXRockwallCountyFParser() {
    super(CITY_LIST, "ROCKWALL COUNTY", "TX");
  }

  private static final String[] CITY_LIST = new String[] {
      "DALLAS",
      "FATE",
      "GARLAND",
      "HEATH",
      "MCLENDON-CHISHOLM",
      "MOBILE CITY",
      "ROCKWALL",
      "ROWLETT",
      "ROYSE CITY",
      "WYLIE"
  };

}
