package net.anei.cadpage.parsers.RI;

import net.anei.cadpage.parsers.GroupBestParser;

/**
* Washington County, RI
 */

public class RIWashingtonCountyParser extends GroupBestParser {
  public RIWashingtonCountyParser() {
    super(new RIWashingtonCountyAParser(), new RIWashingtonCountyBParser());
  }
  
  static final String[] CITY_LIST = new String[]{
    "CHARLESTOWN",
    "CAROLINA",
    "EXETER",
    "HOPKINTON",
    "ASHAWAY",
    "HOPE VALLEY",
    "NARRAGANSETT",
    "GALILEE",
    "NARRAGANSETT PIER",
    "NEW SHOREHAM",
    "NORTH KINGSTOWN",
    "WICKFORD",
    "SAUNDERSTOWN",
    "RICHMOND",
    "CAROLINA",
    "KENYON",
    "SHANNOCK",
    "USQUEPAUG",
    "WYOMING",
    "SOUTH KINGSTOWN",
    "KINGSTON",
    "MATUNUCK",
    "PEACEDALE",
    "WAKEFIELD",
    "WESTERLY",
    "BRADFORD",
    "WATCH HILL",
    "WHITE ROCK"
  };
}
