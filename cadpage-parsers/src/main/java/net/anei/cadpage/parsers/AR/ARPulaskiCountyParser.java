package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Pulaski County, AR
 */
public class ARPulaskiCountyParser extends GroupBestParser {
  public ARPulaskiCountyParser() {
    super(new ARPulaskiCountyCParser(),
          new ARPulaskiCountyDParser(),
          new ARPulaskiCountyEParser());
  }


  static final String[] CITY_LIST = new String[] {

      // Cities
      "CAMMACK VILLAGE",
      "JACKSONVILLE",
      "LITTLE ROCK",
      "MAUMELLE",
      "NORTH LITTLE ROCK",
      "SHERWOOD",
      "WRIGHTSVILLE",

      // Town
      "ALEXANDER",

      // Census-designated places
      "COLLEGE STATION",
      "GIBSON",
      "HENSLEY",
      "LANDMARK",
      "MCALMONT",
      "NATURAL STEPS",
      "ROLAND",
      "SCOTT",
      "SWEET HOME",
      "WOODSON",

      // Other communities
      "CRYSTAL HILL",
      "GRAVEL RIDGE",
      "IRONTON",
      "LITTLE ITALY",
      "MABELVALE",
      "MARCHE",
      "PANKEY",
      "WOODYARDVILLE",

      // Lonoke County
      "CABOT",
      "ENGLAND",

      // Perry County
      "BIGELOW",

      // Saline County
      "PARON"
  };

}
