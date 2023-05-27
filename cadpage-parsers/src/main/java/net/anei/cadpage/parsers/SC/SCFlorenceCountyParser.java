package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class SCFlorenceCountyParser extends DispatchSouthernParser {

  public SCFlorenceCountyParser() {
    super(CITY_LIST, "FLORENCE COUNTY", "SC", DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_X | DSFLG_OPT_CODE | DSFLG_ID | DSFLG_TIME);
    setupMultiWordStreets("I M GRAHAM");
    removeWords("COURT", "HEIGHTS", "PLACE", "STREET");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    body = stripFieldStart(body, "CFS Location:");
    return super.parseMsg(body, data);
  }

  private static final String[] CITY_LIST = new String[]{
    "COWARD",
    "EFFINGHAM",
    "FLORENCE",
    "JOHNSONVILLE",
    "LAKE CITY",
    "MARS BLUFF",
    "OLANTA",
    "PAMPLICO",
    "QUINBY",
    "SCRANTON",
    "TIMMONSVILLE",

    // Charleston County
    "CHARLESTON",

    // Darlingon County
    "LAMAR",

    // Sumter County
    "SUMTER",

    // Williamsburg County
    "WILLIAMSBURG",
    "HEMINGWAY"
  };
}
