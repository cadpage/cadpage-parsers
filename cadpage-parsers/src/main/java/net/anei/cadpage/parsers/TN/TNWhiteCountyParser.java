package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;


public class TNWhiteCountyParser extends DispatchA65Parser {
  
  public TNWhiteCountyParser() {
    super(CITY_LIST, "WHITE COUNTY", "TN");
  }
  
  @Override
  public String getFilter() {
    return "whitecotn@911email.net";
  } 
  
  private static final String[] CITY_LIST = new String[]{
      
      "BON AIR",
      "DOYLE",
      "CASSVILLE",
      "DEROSSETT",
      "QUEBECK",
      "RAVENSCROFT",
      "SPARTA",
      "WALLING",
      "YANKEETOWN"

  };
}
