package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;


public class IAWinnebagoCountyParser extends DispatchA47Parser {
  
  public IAWinnebagoCountyParser() {
    super("Dispatch info", CITY_LIST, "WINNEBAGO COUNTY", "IA", ".*");
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
      "FERTILE"
  };
}
