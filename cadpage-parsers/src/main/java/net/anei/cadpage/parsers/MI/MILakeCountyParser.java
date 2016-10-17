package net.anei.cadpage.parsers.MI;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


/**
 * Lake County, MI
 */
public class MILakeCountyParser extends DispatchOSSIParser {
  
  public MILakeCountyParser() {
    super(CITY_CODES, "LAKE COUNTY", "MI",
          "FYI CALL ADDR! ( END | X/Z+? CITY! END )");
  }
  
  @Override
  public String getFilter() {
    return "CAD@co.lake.mi.us";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BALD", "BALDWIN",
      "CHER", "CHERRY VALLEY",
      "PEAC", "PEACOCK",
      "PLEA", "PLEASANT PLAINS",
      "SWEE", "SWEETWATER",
      "YATE", "YATES"
  });
  
}
