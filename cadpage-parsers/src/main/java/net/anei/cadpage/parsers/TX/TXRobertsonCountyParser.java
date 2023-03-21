package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class TXRobertsonCountyParser extends DispatchSouthernParser {

  public TXRobertsonCountyParser() {
    super(CITY_LIST, "ROBERTSON COUNTY", "TX", 
          DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_NAME | DSFLG_PHONE | DSFLG_CODE | DSFLG_TIME | DSFLG_PROC_EMPTY_FLDS);
  }

  @Override
  public String getFilter() {
    return "rcsodispatch@sheriff.co.robertson.tx.us";
  }
  
  private static final String[] CITY_LIST = new String[] {
      
      // Cities
      "BREMOND",
      "CALVERT",
      "FRANKLIN",
      "HEARNE",
      
      // Unincorporated communities
      "BALD PRAIRIE",
      "BENCHLEY",
      "EASTERLY",
      "ELLIOTT",
      "HAMMOND",
      "MUMFORD",
      "NEW BADEN",
      "RIDGE",
      "TIDWELL PRAIRIE",
      "VALLEY JUNCTION",
      "WHEELOCK",
      
      // Ghost towns
      "OWENSVILLE",
      
      // Brazos County
      "BRIAN",
      
      // Limestone County
      "THORNTON",
      
      // Milam County
      "GAUSE"
  };
}
