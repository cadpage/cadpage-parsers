package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDHarfordCountyParser extends GroupBestParser {

  public MDHarfordCountyParser() {
    super(new MDHarfordCountyAParser(),
          new MDHarfordCountyBParser(),
          new MDHarfordCountyCParser());
  }

  static final String[] CITY_LIST = new String[]{
    "ABERDEEN",
    "BEL AIR",
    "HAVRE DE GRACE",
    "JOPPA",
    "ABINGDON",
    "BELCAMP",
    "LEVEL",
    "CHURCHVILLE",
    "DARLINGTON",
    "WHITEFORD",
    "JARRETTSVILLE",
    "STREET",
    "HICKORY",
    "FALLSTON",
    "JOPPATOWNE",
    "FAWN GROVE",
    "DELTA",
    "PYLESVILLE",
    "WHITE HALL",
    "FOREST HILL"
  };
}
