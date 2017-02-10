
package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class SCMarlboroCountyParser extends DispatchSouthernParser {

  public SCMarlboroCountyParser() {
    super(CITY_LIST, "MARLBORO COUNTY", "SC", 
        DSFLG_ID|DSFLG_ADDR|DSFLG_ADDR_TRAIL_PLACE|DSFLG_TIME);
    removeWords("SQUARE");
  }
  
  private static final String[] CITY_LIST = new String[]{

      "BENNETTSVILLE",
      "BLENHEIM",
      "CLIO",
      "MCCOLL",
      "MC COLL",
      "TATUM",
      "WALLACE",

  };
}
