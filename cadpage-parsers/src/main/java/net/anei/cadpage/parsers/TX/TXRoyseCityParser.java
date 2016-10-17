
package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class TXRoyseCityParser extends DispatchSouthernParser {

  public TXRoyseCityParser() {
    super(CITY_LIST, "ROYSE CITY", "TX", DSFLAG_NO_NAME_PHONE);
  }
  
  private static final String[] CITY_LIST = new String[]{
    "DALLAS",
    "FATE",
    "HEATH",
    "MCLENDON-CHISHOLM",
    "MOBILE CITY",
    "ROCKWALL",
    "ROWLETT",
    "ROYSE CITY",
    "WYLIE"
  };
}
