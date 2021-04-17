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
          "| FYI? CALL ADDR! ( X/Z X/Z CITY | X/Z CITY | CITY | X+? ) INFO/N+ )");
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
      "CH",   "CHASE TWP",
      "CHA",  "CHASE TWP",
      "CHAS", "CHASE TWP",
      "CH",   "CHERRY VALLEY TWP",
      "CHE",  "CHERRY VALLEY TWP",
      "CHER", "CHERRY VALLEY TWP",
      "EDEN", "EDEN TWP",
      "ELK",  "ELK TWP",
      "I",    "IRONS",
      "IR",   "IRONS",
      "IRO",  "IRONS",
      "IRON", "IRONS",
      "P",    "",
      "PE",   "PEACOCK TWP",
      "PEA",  "PEACOCK TWP",
      "PEAC", "PEACOCK TWP",
      "PI",   "PINORA",
      "PIN",  "PINORA TWP",
      "PINO", "PINORA TWP",
      "PL",   "PLEASANT PLAINS TWP",
      "PLE",  "PLEASANT PLAINS TWP",
      "PLEA", "PLEASANT PLAINS TWP",
      "S",    "",
      "SA",   "SAUBLE TWP",
      "SAU",  "SAUBLE TWP",
      "SAUB", "SAUBLE TWP",
      "SW",   "SWEETWATER TWP",
      "SWE",  "SWEETWATER TWP",
      "SWEE", "SWEETWATER TWP",
      "W",    "WEBBER TWP",
      "WE",   "WEBBER TWP",
      "WEB",  "WEBBER TWP",
      "WEBB", "WEBBER TWP",
      "Y",    "YATES TWP",
      "YA",   "YATES TWP",
      "YAT",  "YATES TWP",
      "YATE", "YATES TWP"
  });

}
