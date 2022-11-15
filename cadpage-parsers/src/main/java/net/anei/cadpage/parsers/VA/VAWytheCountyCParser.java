package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class VAWytheCountyCParser extends DispatchSouthernParser {

  public VAWytheCountyCParser() {
    super(CITY_LIST, "WYTHE COUNTY", "VA",
          DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_BAD_PLACE | DSFLG_X | DSFLG_NAME | DSFLG_PHONE | DSFLG_CODE | DSFLG_UNIT1 | DSFLG_ID | DSFLG_TIME);
  }

  private static final String[] CITY_LIST = new String[] {

      // Towns
      "RURAL RETREAT",
      "WYTHEVILLE",

      // Census-designated places
      "FORT CHISWELL",
      "IVANHOE",
      "MAX MEADOWS",

      // Other unincorporated communities
      "AUSTINVILLE",
      "CRIPPLE CREEK",
      "CROCKETT",
      "SPEEDWELL",

      // Carroll County
      "HILLSVILLE",
      "WOODLAWN",

      "GALAX"
  };
}
