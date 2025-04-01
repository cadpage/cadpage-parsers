package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class TXWiseCountyBParser extends DispatchSouthernParser {

  public TXWiseCountyBParser() {
    super(CITY_LIST, "WISE COUNTY", "TX",
          DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_NAME | DSFLG_PHONE | DSFLG_CODE | DSFLG_ID | DSFLG_TIME | DSFLG_PROC_EMPTY_FLDS);
  }

  @Override
  public String getFilter() {
    return "sswadmin@sheriff.co.wise.tx.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "AURORA",
      "BRIDGEPORT",
      "DECATUR",
      "FORT WORTH",
      "LAKE BRIDGEPORT",
      "NEW FAIRVIEW",
      "NEWARK",
      "PARADISE",
      "RHOME",
      "RUNAWAY BAY",

      // Towns
      "ALVORD",
      "BOYD",
      "CHICO",
      "CRAFTON",

      // Census-designated places
      "BRIAR",
      "PECAN ACRES",

      // Unincorporated communities
      "ALLISON",
      "BALSORA",
      "BOONSVILLE",
      "COTTONDALE",
      "GREENWOOD",
      "SLIDELL",
      "SYCAMORE",

      "MONTAGUE COUNTY",
      "SUNSET",

      "PARKER COUNTY",
      "POOLVILLE",

      "TARRANT COUNTY",

      "WISE COUNTY"
  };
}
