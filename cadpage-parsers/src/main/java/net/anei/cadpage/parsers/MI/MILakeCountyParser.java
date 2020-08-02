package net.anei.cadpage.parsers.MI;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


/**
 * Lake County, MI
 */
public class MILakeCountyParser extends DispatchOSSIParser {
  
  public MILakeCountyParser() {
    super(CITY_CODES, "LAKE COUNTY", "MI",
          "( CANCEL ADDR CITY! INFO/N+ " + 
          "| FYI? CALL ADDR! ( END | X/Z+? CITY END ) )");
  }
  
  @Override
  public String getFilter() {
    return "CAD@co.lake.mi.us";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "B",    "BALDWIN",            
      "BA",   "BALDWIN",
      "BAL",  "BALDWIN",
      "BALD", "BALDWIN",
      "C",    "",
      "CH",   "CHASE",
      "CHA",  "CHASE",
      "CHAS", "CHASE",
      "CH",   "CHERRY VALLEY",
      "CHE",  "CHERRY VALLEY",
      "CHER", "CHERRY VALLEY",
      "EDEN", "EDEN",
      "ELK",  "ELK",
      "P",    "",
      "PE",   "PEACOCK",
      "PEA",  "PEACOCK",
      "PEAC", "PEACOCK",
      "PI",   "PINORA",
      "PIN",  "PINORA",
      "PINO", "PINORA",
      "PL",   "PLEASANT PLAINS",
      "PLE",  "PLEASANT PLAINS",
      "PLEA", "PLEASANT PLAINS",
      "S",    "SWEETWATER",
      "SW",   "SWEETWATER",
      "SWE",  "SWEETWATER",
      "SWEE", "SWEETWATER",
      "Y",    "YATES",
      "YA",   "YATES",
      "YAT",  "YATES",
      "YATE", "YATES",
      "W",    "WEBBER",
      "WE",   "WEBBER",
      "WEB",  "WEBBER",
      "WEBB", "WEBBER"
  });
  
}
