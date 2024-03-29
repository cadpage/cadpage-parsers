package net.anei.cadpage.parsers.TN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;


public class TNCoffeeCountyParser extends DispatchA74Parser {

  public TNCoffeeCountyParser() {
    super(CITY_LIST, "COFFEE COUNTY", "TN");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "coffeecotn@911email.net,geoconex@nlamerica.com,dispatch@911comm2.info,@coffeetne911.info";
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "21 POWELL RD",                         "+35.407222,-86.173306"
  });

  private static final String[] CITY_LIST = new String[]{

      "BEECHGROVE",
      "BEECH GROVE",
      "BELMONT",
      "FARRAR HILL",
      "FUDGEAROUND",
      "HILLSBORO",
      "LAKEWOOD PARK",
      "MANCHESTER",
      "MORRISON",
      "NEW UNION",
      "NOAH",
      "POCAHONTAS",
      "SHADY GROVE",
      "SUMMITVILLE",
      "SUMMITTVILLE",
      "TULLAHOMA",

      // Bedford County
      "NORMANDY",
      "WARTRACE",

      // Franklin County
      "ESTILL SPRINGS",

      // Warren County
      "MCMINNVILLE"
  };
}
