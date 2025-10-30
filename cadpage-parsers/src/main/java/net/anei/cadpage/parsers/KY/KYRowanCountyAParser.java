package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class KYRowanCountyAParser extends DispatchA65Parser {
  
  public KYRowanCountyAParser() {
    super(CITY_LIST, "ROWAN COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@911comm1.info";
  }
  
  private static final String[] CITY_LIST = new String[]{

      //CITIES
      
      "LAKEVIEW HEIGHTS",
      "MOREHEAD",

      //OTHER UNINCORPORATED COMMUNITIES

      "CLEARFIELD",
      "CRANSTON",
      "ELLIOTTVILLE",
      "FARMERS",
      "GATES",
      "HALDEMAN",
      "HAYES CROSSING",
      "HILDA",
      "PARAGON",
      "PELFREY",
      "RODBURN",
      "SHARKEY",
      "SMILE",
      "TRIPLETT",
      "WAGNER CORNER"
  };
}
