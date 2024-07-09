package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.GroupBestParser;


public class CTFairfieldCountyParser extends GroupBestParser {

  public CTFairfieldCountyParser() {
    super(new CTFairfieldCountyAParser(),
          new CTFairfieldCountyBParser(),
          new CTFairfieldCountyCParser(),
          new CTFairfieldCountyFParser());
  }

  static String[] CITY_LIST = new String[]{
    "BRIDGEPORT",
    "EASTON",
    "MONROE",
    "SHELTON",
    "STRATFORD",
    "TRUMBULL",
    "WESTON",

    // New Haven County
    "MILFORD"
  };
}
