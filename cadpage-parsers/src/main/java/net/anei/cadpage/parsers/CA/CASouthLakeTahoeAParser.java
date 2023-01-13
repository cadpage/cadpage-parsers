package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA58Parser;

public class CASouthLakeTahoeAParser extends DispatchA58Parser {

  public CASouthLakeTahoeAParser() {
    this("SOUTH LAKE TAHOE", "CA");
  }

  public CASouthLakeTahoeAParser(String defCity, String defState) {
    super("Cyrun Auto Paging", CITY_LIST, defCity, defState);
  }

  @Override
  public String getFilter() {
    return "cyrunpaging@cyrun.com";
  }
  
  private static String[] CITY_LIST = new String[] {
      "MEYERS",
      "S LAKE TAHOE" 
  };

}
