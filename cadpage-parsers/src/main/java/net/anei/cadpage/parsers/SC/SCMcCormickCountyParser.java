package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;


public class SCMcCormickCountyParser extends DispatchA65Parser {
  
  public SCMcCormickCountyParser() {
    super(CITY_LIST, "MCCORMICK COUNTY", "SC");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm1.info,mccormickcosc@911email.net";
  }
  
  private static final String[] CITY_LIST = new String[]{
      
      // Towns
      "MCCORMICK",
      "MC CORMICK",
      "PARKSVILLE",
      "PLUM BRANCH",

      // Census-designated places
      "CLARKS HILL",
      "MODOC",
      "MOUNT CARMEL",
      "WILLINGTON",
      
      // Greenwood County
      "TROY"
  };
}
