package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALLeeCountyParser extends GroupBestParser {

  public ALLeeCountyParser() {
    super(new ALLeeCountyAParser(),
          new ALLeeCountyBParser(),
          new ALLeeCountyCParser());
  }

  static final String[] CITY_LIST = {

    // Cities
    "AUBURN",
    "OPELIKA",
    "PHENIX CITY",
    "SMITH",
    "SMITH STATION",
    "SMITHS",
    "SMITHS STATION",

    // Towns
    "LOACHAPOKA",
    "NOTASULGA",
    "WAVERLY",

    // Unincorporated Communities
    "BEAUREGARD",
    "BEE HIVE",
    "BEULAH",
    "CHEWACLA",
    "GOLD HILL",
    "HOPEWELL",
    "MARVYN",
    "ROXANA",
    "SALEM",
    "THE BOTTLE",

    // Chambers County
    "CUSSETA",
    "VALLEY"
  };

}
