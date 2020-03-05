package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;


public class TNCoffeeCountyParser extends DispatchA65Parser {
  
  public TNCoffeeCountyParser() {
    super(CITY_LIST, "COFFEE COUNTY", "TN");
  }
  
  @Override
  public String getFilter() {
    return "coffeecotn@911email.net,geoconex@nlamerica.com,dispatch@911comm2.info,@coffeetne911.info";
  } 
  
  private static final String[] CITY_LIST = new String[]{

      "BEECHGROVE",
      "BELMONT",
      "FUDGEAROUND",
      "HILLSBORO",
      "LAKEWOOD PARK",
      "MANCHESTER",
      "NEW UNION",
      "NOAH",
      "POCAHONTAS",
      "SHADY GROVE",
      "SUMMITVILLE",
      "TULLAHOMA"

  };
}
