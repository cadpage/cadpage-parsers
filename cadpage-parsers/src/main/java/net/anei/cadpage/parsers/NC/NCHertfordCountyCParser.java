
package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCHertfordCountyCParser extends DispatchSouthernParser {

  public NCHertfordCountyCParser() {
    super(CITY_LIST, "HERTFORD COUNTY", "NC", 
        DSFLG_PROC_EMPTY_FLDS | DSFLG_ID | DSFLG_ADDR | DSFLG_X |DSFLG_NAME | DSFLG_PHONE | DSFLG_ID | DSFLG_TIME);
  }
 
  private static final String[] CITY_LIST = new String[]{

      "AHOSKIE",
      "COFIELD",
      "COMO",
      "HARRELLSVILLE",
      "MURFREESBORO",
      "WINTON"

  };
}
