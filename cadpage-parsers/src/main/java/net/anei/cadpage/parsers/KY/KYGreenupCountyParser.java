package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;



public class KYGreenupCountyParser extends DispatchB2Parser {
  
  public KYGreenupCountyParser() {
    super("GCE911:", CITY_LIST, "GREENUP COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "GCE911@GCE911";
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
    "BELLEFONTE",
    "FLATWOODS",
    "GREENUP",
    "RACELAND",
    "RUSSELL",
    "SOUTH SHORE",
    "WORTHINGTON",
    "WURTLAND"
  };


}
