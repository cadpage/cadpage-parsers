package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;

public class IACherokeeCountyParser extends DispatchA47Parser {

  public IACherokeeCountyParser() {
    super("Comm Center", CITY_LIST, "CHEROKEE COUNTY", "IA", null);
  }

  @Override
  public String getFilter() {
    return "swmail@cherokeecoso.us";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "AURELIA",
      "CHEROKEE",
      "CLEGHORN",
      "LARRABEE",
      "MARCUS",
      "MERIDEN",
      "QUIMBY",
      "WASHTA",

      // Townships
      "AFTON",
      "AMHERST",
      "CEDAR",
      "CHEROKEE",
      "DIAMOND",
      "GRAND MEADOW",
      "LIBERTY",
      "MARCUS",
      "PILOT",
      "PITCHER",
      "ROCK",
      "SHERIDAN",
      "SILVER",
      "SPRING",
      "TILDEN",
      "WILLOW"
  };
}
