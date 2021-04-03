package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;



public class NCHarnettCountyParser extends GroupBestParser {

  public NCHarnettCountyParser() {
    super(new NCHarnettCountyAParser(),
          new NCHarnettCountyBParser(),
          new NCHarnettCountyCParser(),
          new NCHarnettCountyDParser());
  }

  static final String[] CITY_LIST = new String[]{

    // Townships
    "ANDERSON CREEK",
    "AVERASBORO",
    "BARBECUE",
    "BLACK RIVER",
    "BUCKHORN",
    "DUKE",
    "GROVE",
    "HECTORS CREEK",
    "JOHNSONVILLE",
    "LILLINGTON",
    "NEILLS CREEK",
    "STEWARTS CREEK",
    "UPPER LITTLE RIVER",

    // Cities and towns
    "CAMERON",
    "COATS",
    "DUNN",
    "ERWIN",
    "LILLINGTON",
    "SPRING LAKE",

    // Unincorporated Communities
    "ANDERSON CREEK",
    "BARBECUE",
    "BARCLAYSVILLE",
    "BUIES CREEK",
    "BUNNLEVEL",
    "CAPE FEAR",
    "CHALYBEATE SPRINGS",
    "COKESBURY",
    "DUNCAN",
    "JOHNSONVILLE",
    "HARNETT",
    "KIPLING",
    "LUART",
    "MAMERS",
    "OLIVIA",
    "OVERHILLS",
    "PINEVIEW",
    "RYES",
    "SEMINOLE",
    "SHAWTOWN",
    "SPOUT SPRINGS",
    "TURLINGTON",

    // Other
    "LINDEN",
    "ANGIER",
    "BENSON",

    // Cumberland County
    "FAYETTEVILLE",

    // Lee County
    "BROADWAY",
    "SANFORD",

    // Moore County
    "ABERDEEN",
    "SOUTHERN PINES",
    "VASS",

    // Wake County
    "HOLLY SPRINGS",
    "FUQUAY",
    "FUQUAY VARINA",
    "FUQUAY-VARINA",
    "WILLOW SPRING",
    "WILLOW SPRINGS"
  };
}
