package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Cuyahoga County, OH
 */

public class OHCuyahogaCountyParser extends GroupBestParser {
  
  public OHCuyahogaCountyParser() {
    super(new OHCuyahogaCountyAParser(), new OHCuyahogaCountyBParser(), new OHCuyahogaCountyCParser(),
          new OHCuyahogaCountyDParser(), new OHCuyahogaCountyFParser());
  }
  
  static final Properties CITY_CODES = buildCodeTable(new String[]{

      //Cities
      "BAY VILLAGE",   "BAY VILLAGE",
      "BEACHWOOD",     "BEACHWOOD",
      "BEDFORD",       "BEDFORD",
      "BEDFORD HTS",   "BEDFORD HEIGHTS",
      "BEDFORD HEIG",  "BEDFORD HEIGHTS",
      "BEREA",         "BEREA",
      "BRECKSVILLE",   "BRECKSVILLE",
      "BROADVIEW HE",  "BROADVIEW HEIGHTS",
      "BROADVIEW HTS", "BROADVIEW HEIGHTS",
      "BROOK PARK",    "BROOK PARK",
      "BROOKLYN",      "BROOKLYN",
      "CLEVELAND",     "CLEVELAND",
      "CLEVELAND HE",  "CLEVELAND HEIGHTS",
      "CLEVELAND HTS", "CLEVELAND HEIGHTS",
      "E CLEVELAND",   "E CLEVELAND",
      "EUCLID",        "EUCLID",
      "FAIRVIEW PAR",  "FAIRVIEW PARK",
      "GARFIELD HEI",  "GARFIELD HEIGHTS",
      "GARFIELD HTS",  "GARFIELD HEIGHTS",
      "HIGHLAND HEI",  "HIGHLAND HEIGHTS",
      "HIGHLAND HTS",  "HIGHLAND HEIGHTS",
      "INDEPENDENCE",  "INDEPENDENCE",
      "LAKEWOOD",      "LAKEWOOD",
      "LYNDHURST",     "LYNDHURST",
      "MAPLE HEIGHT",  "MAPLE HEIGHTS",
      "MAPLE HTS",     "MAPLE HEIGHTS",
      "MAYFIELD HEI",  "MAYFIELD HEIGHTS",
      "MAYFIELD HTS",  "MAYFIELD HEIGHTS",
      "MIDDLEBURG H",  "MIDDLEBURG HEIGHTS",
      "MIDDLEBURG HTS","MIDDLEBURG HEIGHTS",
      "N OLMSTED",     "N OLMSTED",
      "N ROYALTON",    "N ROYALTON",
      "OLMSTED FALL",  "OLMSTED FALLS",
      "PARMA",         "PARMA",
      "PARMA HTS",     "PARMA HEIGHTS",
      "PARMA HEIGHT",  "PARMA HEIGHTS",
      "PEPPER PIKE",   "PEPPER PIKE",
      "RICHMOND HEI",  "RICHMOND HEIGHTS",
      "RICHMOND HTS",  "RICHMOND HEIGHTS",
      "ROCKY RIVER",   "ROCKY RIVER",
      "SEVEN HILLS",   "SEVEN HILLS",
      "SHAKER HEIGH",  "SHAKER HEIGHTS",
      "SHAKER HTS",    "SHAKER HEIGHTS",
      "SOLON",         "SOLON",
      "S EUCLID",      "S EUCLID",
      "S RUSSELL",     "S RUSSELL",
      "STRONGSVILLE",  "STRONGSVILLE",
      "UNIVERSITY H",  "UNIVERSITY HEIGHTS",
      "UNIVERSITY HTS","UNIVERSITY HEIGHTS",
      "WARRENSVILLE",  "WARRENSVILLE HEIGHTS",
      "WARRENSVILLE HTS","WARRENSVILLE HEIGHTS",
      "WESTLAKE",      "WESTLAKE",

      //VILLAGES
      "BENTLEYVILLE",  "BENTLEYVILLE",
      "BRATENAHL",     "BRATENAHL",
      "BROOKLYN HEI",  "BROOKLYN HEIGHTS",
      "BROOKLYN HTS",  "BROOKLYN HEIGHTS",
      "CHAGRIN FALL",  "CHAGRIN FALLS",
      "CUYAHOGA HEI",  "CUYAHOGA HEIGHTS",
      "CUYAHOGA HTS",  "CUYAHOGA HEIGHTS",
      "GATES MILLS",   "GATES MILLS",
      "GLENWILLOW",    "GLENWILLOW",
      "HIGHLAND HIL",  "HIGHLAND HILLS",
      "HUNTING VALL",  "HUNTING VALLEY",
      "LINNDALE",      "LINNDALE",
      "MAYFIELD",      "MAYFIELD",
      "MORELAND HIL",  "MORELAND HILLS",
      "NEWBURGH HEI",  "NEWBURGH HEIGHTS",
      "NEWBURGH HTS",  "NEWBURGH HEIGHTS",
      "N RANDALL",     "N RANDALL",
      "NORTHFIELD VIL","NORTHFIELD",
      "OAKWOOD",       "OAKWOOD",
      "OAKWOOD VIL",   "OAKWOOD",
      "ORANGE",        "ORANGE",
      "VALLEY VIEW",   "VALLEY VIEW",
      "WALTON HILLS",  "WALTON HILLS",
      "WOODMERE",      "WOODMERE",

      //TOWNSHIPS
      "CHAGRIN FALL",  "CHAGRIN FALLS TWP",
      "OLMSTED",       "OLMSTED TWP",

  });
  
  static final String[] CITY_LIST = new String[]{

    // Cities
    "BAY VILLAGE",
    "BEACHWOOD",
    "BEDFORD",
    "BEDFORD HEIGHTS",
    "BEREA",
    "BRECKSVILLE",
    "BROADVIEW HEIGHTS",
    "BROOK PARK",
    "BROOKLYN",
    "CLEVELAND (COUNTY SEAT)",
    "CLEVELAND HEIGHTS",
    "EAST CLEVELAND",
    "EUCLID",
    "FAIRVIEW PARK",
    "GARFIELD HEIGHTS",
    "HIGHLAND HEIGHTS",
    "INDEPENDENCE",
    "LAKEWOOD",
    "LYNDHURST",
    "MAPLE HEIGHTS",
    "MAYFIELD HEIGHTS",
    "MIDDLEBURG HEIGHTS",
    "NORTH OLMSTED",
    "NORTH ROYALTON",
    "OLMSTED FALLS",
    "PARMA",
    "PARMA HEIGHTS",
    "PEPPER PIKE",
    "RICHMOND HEIGHTS",
    "ROCKY RIVER",
    "SEVEN HILLS",
    "SHAKER HEIGHTS",
    "SOLON",
    "SOUTH EUCLID",
    "STRONGSVILLE",
    "TWINSBURG",
    "UNIVERSITY HEIGHTS",
    "WARRENSVILLE HEIGHTS",
    "WESTLAKE",

    // Villages
    "BENTLEYVILLE",
    "BRATENAHL",
    "BROOKLYN HEIGHTS",
    "CHAGRIN FALLS",
    "CUYAHOGA HEIGHTS",
    "GATES MILLS",
    "GLENWILLOW",
    "HIGHLAND HILLS",
    "HUNTING VALLEY",
    "LINNDALE",
    "MAYFIELD",
    "MORELAND HILLS",
    "NEWBURGH HEIGHTS",
    "NORTH RANDALL",
    "OAKWOOD",
    "ORANGE",
    "VALLEY VIEW",
    "WALTON HILLS",
    "WOODMERE",

    // Townships
    "CHAGRIN FALLS TWP",
    "OLMSTED TWP",
    
    // Summit County
    "MACEDONIA",
    "NORTHFIELD",
    "RICHFIELD"
  };
}
