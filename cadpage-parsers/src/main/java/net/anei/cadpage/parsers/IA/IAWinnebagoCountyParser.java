package net.anei.cadpage.parsers.IA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;


public class IAWinnebagoCountyParser extends DispatchA47Parser {
  
  public IAWinnebagoCountyParser() {
    super("Dispatch info", CITY_LIST, "WINNEBAGO COUNTY", "IA", ".*");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "911@winncosheriff.org";
  }

  private static final String[] CITY_LIST =new String[]{

      //Cities
      "BUFFALO CENTER",
      "FOREST CITY",
      "LAKE MILLS",
      "LELAND",
      "RAKE",
      "SCARVILLE",
      "THOMPSON",

      //Townships
      "BUFFALO",
      "CENTER",
      "EDEN",
      "FOREST",
      "GRANT",
      "KING",
      "LINCOLN",
      "LINDEN",
      "LOGAN",
      "MOUNT VALLEY",
      "NEWTON",
      "NORWAY",
      
      // Worth County
      "FERTILE",
      
      // Cerro Gordo County
      "BRITT",
      "MASON CITY",
      
      // Freeborn County, MN
      "ALBERT LEA",
      
      // Faribault County, MN
      "BLUE EARTH",
      
      // Far far away
      "IOWA CITY",
      "DES MOINES",
      "ROCHESTER"
  };
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "34535 GREEN ACRES DR",                 "+43.265180,-93.618451"
  });
}
