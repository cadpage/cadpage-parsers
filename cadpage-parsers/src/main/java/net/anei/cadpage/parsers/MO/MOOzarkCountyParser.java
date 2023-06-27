package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class MOOzarkCountyParser extends DispatchA33Parser {

  public MOOzarkCountyParser() {
    super(CITY_LIST, "OZARK COUNTY", "MO", A33_X_ADDR_EXT);
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

  private static final String[] CITY_LIST = new String[] {

      // City
      "GAINESVILLE",

      // Villages
      "BAKERSFIELD",
      "THEODOSIA",

      /// Census-designated places
      "PONTIAC",
      "SUNDOWN",
      "WASOLA",

      /// Unincorporated communities
      "ALMARTHA",
      "BRIXEY",
      "DORA",
      "DUGGINSVILLE",
      "ELIJAH",
      "FOIL",
      "HAMMOND",
      "HARDENVILLE",
      "HOWARDS RIDGE",
      "ISABELLA",
      "LONGRUN",
      "LUTIE",
      "MAMMOTH",
      "NOBLE",
      "NOTTINGHILL",
      "OCIE",
      "ROCKBRIDGE",
      "ROMANCE",
      "SOUDER",
      "SYCAMORE",
      "TECUMSEH"
  };
}
