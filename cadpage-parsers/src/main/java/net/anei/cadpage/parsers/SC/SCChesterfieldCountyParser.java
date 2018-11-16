
package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class SCChesterfieldCountyParser extends DispatchSouthernParser {

  public SCChesterfieldCountyParser() {
    super(CITY_LIST, "CHESTERFIELD COUNTY", "SC", DSFLG_ADDR | DSFLG_X | DSFLG_NAME | DSFLG_PHONE |
        DSFLG_UNIT1 | DSFLG_ID | DSFLG_TIME | DSFLG_PROC_EMPTY_FLDS);
  }

  private static final String[] CITY_LIST = new String[]{

      //TOWNS
      "CHERAW",
      "CHESTERFIELD",
      "JEFFERSON",
      "MCBEE",
      "MOUNT CROGHAN",
      "PAGELAND",
      "PATRICK",
      "RUBY",

      //UNINCORPORATED COMMUNITIES

      "ANGELUS",
      "CASH",
      "MIDDENDORF",
      "MINDEN"
  };
}
