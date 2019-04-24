package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.dispatch.DispatchA13Parser;



public class LAWestBatonRougeParishParser extends DispatchA13Parser {
  
  public LAWestBatonRougeParishParser() {
    super(CITY_LIST, "WEST BATON ROUGE PARISH", "LA", A13_FLG_TRAIL_PLACE);
    setupMultiWordStreets("I-10 FRONTAGE");
    setupSpecialStreets(
        "AVENUE A",
        "AVENUE B",
        "AVENUE C",
        "AVENUE D",
        "AVENUE E",
        "AVENUE G",
        "BOULEVARD ACADIEN",
        "RUE DAUPHINE",
        "RUE GRAND PRE",
        "RUE ILE ST JEAN",
        "RUE NOUVELLE ECOSSE",
        "RUE ST PATRICK"
    );
  }
  
  @Override
  public String getFilter() {
    return "@wbrcouncil.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }
  
  private static final String[] CITY_LIST = new String[]{
    "ADDIS",
    "BRUSLY",
    "PORT ALLEN",
    "ANTONIO",
    "BELMONT",
    "BUECHE",
    "CHAMBERLIN",
    "DEVALLS",
    "ERWINVILLE",
    "ITHRA",
    "KAHNS",
    "LOBDELL",
    "LUKEVILLE"
  };
}
