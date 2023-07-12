package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class CAShastaCountyCParser extends DispatchA19Parser {

  public CAShastaCountyCParser() {
    super(CITY_CODES, "SHASTA COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "SHASCOM@ipsshasta.com";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BK", "BUCKEYE",
      "RE", "REDDING",
      "WM", "WHITMORE",

      "SC1", "",
      "SC2", "",
      "SC3", "",
      "SC4", "",
      "SC5", "",
      "SC6", "",
      "SC7", ""

  });
}
