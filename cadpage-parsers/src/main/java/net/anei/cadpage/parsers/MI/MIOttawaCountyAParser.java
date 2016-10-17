package net.anei.cadpage.parsers.MI;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA5Parser;


public class MIOttawaCountyAParser extends DispatchA5Parser {
  

  public MIOttawaCountyAParser() {
    super(CITY_ABBRV, "OTTAWA COUNTY", "MI");
  }
  
  @Override
  public String getFilter() {
    return "@OCCDA.ORG";
  }
  
  private static Properties CITY_ABBRV = buildCodeTable(new String[]{
      "HollandCit", "Holland"
  });
  
}
