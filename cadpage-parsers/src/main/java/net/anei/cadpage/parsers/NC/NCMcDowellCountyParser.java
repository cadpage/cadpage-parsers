package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCMcDowellCountyParser extends GroupBestParser {
  
  public NCMcDowellCountyParser() {
    super(new NCMcDowellCountyAParser(), new NCMcDowellCountyBParser());
  }
  
  static String doAdjustMapCity(String city) {
    city = city.toUpperCase();
    city = stripFieldEnd(city, " AREA");
    return convertCodes(city, MAP_CITY_TABLE);
  }
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "ASHFORD",            "MARION",
      "HANKINS NORTH FORK", "MARION",
      "NORTH ASHFORD",      "MARION",
      "NORTH WOODLAWN",     "MARION",
      "PG",                 "MARION",
      "PLEASANT GARDEN",    "MARION",
      "SUGAR HILL",         "MARION",
      "WOODLAWN",           "MARION"
  });

}
