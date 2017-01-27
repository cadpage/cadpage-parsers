
package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class SCMarlboroCountyParser extends DispatchSouthernParser {

  public SCMarlboroCountyParser() {
    super(CITY_LIST, "MARLBORO COUNTY", "SC", 
        DSFLG_ADDR|DSFLG_ADDR_NO_IMPLIED_APT|DSFLG_ID|DSFLG_TIME);
  }
  
  private static final String[] CITY_LIST = new String[]{

      "BENNETTSVILLE",
      "BLENHEIM",
      "CLIO",
      "MCCOLL",
      "TATUM",
      "WALLACE",

  };
}
