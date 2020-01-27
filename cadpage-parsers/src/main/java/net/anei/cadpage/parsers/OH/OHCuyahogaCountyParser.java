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
      "BEDFORD HTS",   "BEDFORD HEIGHTS",
      "BEDFORD HEIG",  "BEDFORD HEIGHTS",
      "BROADVIEW HE",  "BROADVIEW HEIGHTS",
      "BROADVIEW HTS", "BROADVIEW HEIGHTS",
      "CLEVELAND HE",  "CLEVELAND HEIGHTS",
      "CLEVELAND HTS", "CLEVELAND HEIGHTS",
      "FAIRVIEW PAR",  "FAIRVIEW PARK",
      "GARFIELD HEI",  "GARFIELD HEIGHTS",
      "GARFIELD HTS",  "GARFIELD HEIGHTS",
      "HIGHLAND HEI",  "HIGHLAND HEIGHTS",
      "HIGHLAND HTS",  "HIGHLAND HEIGHTS",
      "MAPLE HEIGHT",  "MAPLE HEIGHTS",
      "MAPLE HTS",     "MAPLE HEIGHTS",
      "MAYFIELD HEI",  "MAYFIELD HEIGHTS",
      "MAYFIELD HTS",  "MAYFIELD HEIGHTS",
      "MIDDLEBURG H",  "MIDDLEBURG HEIGHTS",
      "MIDDLEBURG HTS","MIDDLEBURG HEIGHTS",
      "N OLMSTED",     "NORTH OLMSTED",
      "N RIDGEVILLE",  "NORTH RIDGEVILLE",
      "N ROYALTON",    "NORTH ROYALTON",
      "OLMSTED FALL",  "OLMSTED FALLS",
      "PARMA HTS",     "PARMA HEIGHTS",
      "PARMA HEIGHT",  "PARMA HEIGHTS",
      "PEPPER PIKE",   "PEPPER PIKE",
      "RICHMOND HEI",  "RICHMOND HEIGHTS",
      "RICHMOND HTS",  "RICHMOND HEIGHTS",
      "ROCKY RIVER",   "ROCKY RIVER",
      "SEVEN HILLS",   "SEVEN HILLS",
      "SHAKER HTS",    "SHAKER HEIGHTS",
      "S EUCLID",      "SOUTH EUCLID",
      "S RUSSELL",     "SOUTH RUSSELL",
      "UNIVERSITY H",  "UNIVERSITY HEIGHTS",
      "UNIVERSITY HTS","UNIVERSITY HEIGHTS",
      "WARRENSVILLE",  "WARRENSVILLE HEIGHTS",
      "WARRENSVILLE HTS","WARRENSVILLE HEIGHTS",
      "WARRESNVILLE HTS","WARRENSVILLE HEIGHTS",  // Misspelled

      //VILLAGES
      "BROOKLYN HEI",  "BROOKLYN HEIGHTS",
      "BROOKLYN HTS",  "BROOKLYN HEIGHTS",
      "CUYAHOGA HEI",  "CUYAHOGA HEIGHTS",
      "CUYAHOGA HTS",  "CUYAHOGA HEIGHTS",
      "HIGHLAND HIL",  "HIGHLAND HILLS",
      "HUNTING VALL",  "HUNTING VALLEY",
      "MAYFIELD",      "MAYFIELD",
      "MAYFIELD HTS",  "MAYFIELD HEIGHTS",
      "MAYFIELD VILLAGE", "MAYFIELD",
      "MAYFIELD VLG",  "MAYFIELD",
      "MORELAND HIL",  "MORELAND HILLS",
      "NEWBURGH HEI",  "NEWBURGH HEIGHTS",
      "NEWBURGH HTS",  "NEWBURGH HEIGHTS",
      "N RANDALL",     "NORTH RANDALL",
      "NORTH RANDAL",  "NORTH RANDALL",
      "NORTHFIELD VIL","NORTHFIELD",
      "OAKWOOD VIL",   "OAKWOOD",
      "OAKWOOD VILLAGE", "OAKWOOD",
      "ORANGE VILLA",  "ORANGE",

      //TOWNSHIPS
      "CHAGRIN FALL",  "CHAGRIN FALLS TWP",
      "OLMSTED",       "OLMSTED TWP",
      
      // Lake County
      "WLBY HILLS",    "WILLOUGHBY HILLS"

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
    "CLEVELAND",
    "CLEVELAND HEIGHTS",
    "E CLEVELAND",
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
    "PENINSULA",
    "VALLEY VIEW",
    "WALTON HILLS",
    "WOODMERE",

    // Townships
    "CHAGRIN FALLS TWP",
    "OLMSTED TWP",
    
    // Summit County
    "MACEDONIA",
    "NORTHFIELD",
    "RICHFIELD",
    
    // Geauga County
    "BAINBRIDGE",
  };
}
