package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCPersonCountyParser extends GroupBestParser {

  public NCPersonCountyParser() {
    super(new NCPersonCountyAParser(), new NCPersonCountyBParser());
  }


  static final String[] CITY_LIST = new String[]{
    "ROXBORO",

    "ALLENSVILLE",
    "BUSHY FORK",
    "CUNNINGHAM",
    "FLAT RIVER",
    "HOLLOWAY",
    "MT TIRZAH",
    "OLIVE HILL",
    "WOODSDALE",

    "HURDLE MILLS",
    "LEASBURG",
    "ROUGEMONT",
    "SEMORA",
    "TIMBERLAKE",

    // Allamance County
    "ALLAMANCE COUNTY",
    "MEBANE",

    // Caswell County
    "CASWELL COUNTY",
    "BLANCH",
    "MILTON",
    "PROSPECT HILL",
    "YANCEYVILLE",

    // Granville County
    "GRANVILLE COUNTY",
    "OXFORD"
  };

}
