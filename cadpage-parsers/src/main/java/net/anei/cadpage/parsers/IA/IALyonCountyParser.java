package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;

public class IALyonCountyParser extends DispatchA47Parser {

  public IALyonCountyParser() {
    super("LCSO Dispatch", CITY_LIST, "LYON COUNTY", "IA", null);
  }

  @Override
  public String getFilter() {
    return "swmail@lyoncountyia.com";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "ALVORD",
      "DOON",
      "GEORGE",
      "INWOOD",
      "LARCHWOOD",
      "LESTER",
      "LITTLE ROCK",
      "ROCK RAPIDS",

      // Unincorporated communities
      "BELOIT",
      "EDNA",
      "GRANITE",
      "KLONDIKE",

      // Townships
      "ALLISON",
      "CENTENNIAL",
      "CLEVELAND",
      "DALE",
      "DOON",
      "ELGIN",
      "GARFIELD",
      "GRANT",
      "LARCHWOOD",
      "LIBERAL",
      "LOGAN",
      "LYON",
      "MIDLAND",
      "RICHLAND",
      "RIVERSIDE",
      "ROCK",
      "SIOUX",
      "WHEELER"
  };

}
