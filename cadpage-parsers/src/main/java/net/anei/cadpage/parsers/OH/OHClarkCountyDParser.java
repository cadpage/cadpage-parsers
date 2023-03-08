package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class OHClarkCountyDParser extends DispatchEmergitechParser {

  public OHClarkCountyDParser() {
    super("Dispatch:", OHClarkCountyParser.CITY_LIST, "CLARK COUNTY", "OH", TrailAddrType.INFO);
  }
  
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
