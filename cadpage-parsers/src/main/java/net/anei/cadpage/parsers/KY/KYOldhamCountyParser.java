package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYOldhamCountyParser extends GroupBestParser {

  public KYOldhamCountyParser() {
    super(new KYOldhamCountyAParser(),
          new KYOldhamCountyBParser(),
          new KYOldhamCountyCParser());
  }

  static final String[] CITY_LIST = new String[]{
    "BALLARDSVILLE",
    "BROWNSBORO",
    "BUCKNER",
    "CENTERFIELD",
    "CRESTWOOD",
    "FLOYDSBURG",
    "GOSHEN",
    "LAGRANGE",
    "LA GRANGE",
    "ORCHARD GRASS HILLS",
    "PARK LAKE",
    "PEWEE VALLEY",
    "PROSPECT",
    "RIVER BLUFF",
    "WESTPORT",

    // Jefferson County
    "LOUISVILLE",

    // Shelby County
    "SHELBYVILLE",

    // Trimble County
    "BEDFORD"
  };
}
