package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCGreenvilleCountyParser extends GroupBestParser {

  public SCGreenvilleCountyParser() {
          new SCGreenvilleCountyEParser();
  }

  static final String[] CITY_LIST = new String[]{

      // Cities
      "FOUNTAIN INN",
      "GREENVILLE",
      "GREER",
      "MAULDIN",
      "SIMPSONVILLE",
      "TRAVELERS REST",

      // Census-designated places
      "BEREA",
      "CITY VIEW",
      "DUNEAN",
      "FIVE FORKS",
      "GANTT",
      "GOLDEN GROVE",
      "JUDSON",
      "PARKER",
      "PIEDMONT",
      "SANS SOUCI",
      "MARIETTA",
      "TAYLORS",
      "TIGERVILLE",
      "WADE HAMPTON",
      "WARE PLACE",
      "WELCOME",

      // Unincorporated communities
      "CLEVELAND",
      "CONESTEE",

      // Anderson County
      "BELTON",
      "HONEA PATH",
      "PELZER",

      // Laurens County
      "GRAY COURT",

      // Spartanburg County
      "CAMPOBELLO",
      "GREER",
      "LANDRUM",
      "WOODRUFF",

      ""    // Empty city is valid
  };
}