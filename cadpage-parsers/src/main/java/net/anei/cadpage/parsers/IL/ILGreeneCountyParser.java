package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchA29Parser;

public class ILGreeneCountyParser extends DispatchA29Parser {
 
  public   ILGreeneCountyParser() {
    super(CITY_LIST, "GREENE COUNTY", "IL");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@jacksonvilleil.com.com";
  }


  private static final String[] CITY_LIST = new String[]{
      
  // CITIES

      "CARROLLTON",
      "GREENFIELD",
      "ROODHOUSE",
      "WHITE HALL",

  // VILLAGES

      "ELDRED",
      "HILLVIEW",
      "KANE",
      "ROCKBRIDGE",
      "WILMINGTON",

  // UNINCORPORATED COMMUNITIES

      "BARROW",
      "BELLTOWN",
      "BERDAN",
      "DRAKE",
      "EAST HARDIN",
      "LAKE CENTRALIA",
      "OLD KANE",

 // TOWNSHIPS

      "ATHENSVILLE",
      "BLUFFDALE",
      "CARROLLTON",
      "KANE",
      "LINDER",
      "PATTERSON",
      "ROCKBRIDGE",
      "ROODHOUSE",
      "RUBICON",
      "WALKERVILLE",
      "WHITE HALL",
      "WOODVILLE",
      "WRIGHTS"
  };
}
