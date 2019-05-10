package net.anei.cadpage.parsers.OR;


import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA22Parser;



public class ORJeffersonCountyParser extends DispatchA22Parser {
  
  public ORJeffersonCountyParser() {
    this("JEFFERSON COUNTY", "OR");
  }
  
  protected ORJeffersonCountyParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState);
  }
  
  @Override
  public String getAliasCode() {
    return "ORJeffersonCounty";
  }

  @Override
  public String getFilter() {
    return "april.steam@tcdispatch.org,eis@wstribes.org,cad@frontier911.org";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ARL",  "ARLINGTON",
      "CON",  "CONDON",
      "LON",  "LONEROCK",
      "GV",   "GRASS VALLEY",
      "WS",   "WARM SPRINGS"
  });
}
