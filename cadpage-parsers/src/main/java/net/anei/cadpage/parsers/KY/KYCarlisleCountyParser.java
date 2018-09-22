package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class KYCarlisleCountyParser extends DispatchA65Parser {
  
  public KYCarlisleCountyParser() {
    super(CITY_LIST, "CARLISLE COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "@911comm1.info";
  }
  
  private static final String[] CITY_LIST = new String[]{

      "ARLINGTON",
      "BARDWELL",
      "CUNNINGHAM",
      "GEVEDEN",
      "MILBURN",
      "MISSISSIPPI",
      "YELLOW DOG ROAD",
      
      // Ballard County
      "WICKLIFFE"

  };
}
