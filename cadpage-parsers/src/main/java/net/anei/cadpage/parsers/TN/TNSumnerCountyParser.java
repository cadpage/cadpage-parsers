package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNSumnerCountyParser extends GroupBestParser {

  public TNSumnerCountyParser() {
    super(new TNSumnerCountyAParser(), new TNSumnerCountyBParser(),
          new TNSumnerCountyCParser());
  }

  static final String[] CITY_LIST = new String[]{

      // Cities
      "GALLATIN",
      "GOODLETTSVILLE",
      "HENDERSONVILLE",
      "HENDERSONVIL",
      "MILLERSVILLE",
      "MITCHELLVILLE",
      "PORTLAND",
      "WHITE HOUSE",

      // Town
      "WESTMORELAND",

      // Census-designated places
      "BETHPAGE",
      "BRANSFORD",
      "CASTALIAN SPRING",
      "CASTALIAN SPRINGS",
      "COTTONTOWN",
      "FAIRFIELD",
      "GRABALL",
      "NEW DEAL",
      "OAK GROVE",
      "SHACKLE ISLAND",
      "WALNUT GROVE",

      // Unincorporated communities
      "BON AIR",
      "BRACKENTOWN",
      "CAIRO",
      "CORINTH"

  };

}
