package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA58Parser;

public class CASouthLakeTahoeParser extends DispatchA58Parser {

  public CASouthLakeTahoeParser() {
    super("Cyrun Auto Paging", CITY_LIST, "SOUTH LAKE TAHOE", "CA");
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
