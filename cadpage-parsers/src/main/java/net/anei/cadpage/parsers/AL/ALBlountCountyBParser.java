package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ALBlountCountyBParser extends DispatchSouthernParser {

  public ALBlountCountyBParser() {
    super(ALBlountCountyParser.CITY_LIST, "BLOUNT COUNTY", "AL",
         DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_X | DSFLG_NAME | DSFLG_PHONE | DSFLG_CODE | DSFLG_ID | DSFLG_TIME);
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.endsWith("MM")) return true;
    return super.isNotExtraApt(apt);
  }

}
