package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class TXDallasCountyFParser extends DispatchSouthernParser {
  
  public TXDallasCountyFParser() {
    super(TXDallasCountyParser.CITY_LIST, "DALLAS COUNTY", "TX",
          DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_X | DSFLG_OPT_CODE | DSFLG_ID |  DSFLG_TIME);
  }

}
