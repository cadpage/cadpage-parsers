package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ALPikeCountyAParser extends DispatchSouthernParser {

  public ALPikeCountyAParser() {
    this("PIKE COUNTY", "AL");
  }

  ALPikeCountyAParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState,
          DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_X | DSFLG_OPT_NAME | DSFLG_OPT_PHONE | DSFLG_OPT_CODE | DSFLG_ID | DSFLG_TIME | DSFLG_PROC_EMPTY_FLDS);
    removeWords("PIKE", "PLACE");
  }

  @Override
  public String getFilter() {
    return "2183500392";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Pike County 9-1-1:");
    return super.parseMsg(body, data);
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final String[] CITY_LIST = new String[] {

      // Pike County
      // Cities
      "BRUNDIDGE",
      "TROY",

      // Towns
      "BANKS",
      "GOSHEN",

      // Unincorporated communities
      "CHINA GROVE",
      "CURRY",
      "HENDERSON",
      "JONESVILLE",
      "JOSIE",
      "KENT",
      "NEEDMORE",
      "ORION",
      "PRONTO",
      "SHADY GROVE",
      "SPRING HILL",

      // Butler County
      // City
      "GREENVILLE",

      // Towns
      "GEORGIANA",
      "MCKENZIE",

      // Unincorporated communities
      "BOLLING",
      "CHAPMAN",
      "FOREST HOME",
      "GARLAND",
      "INDUSTRY",
      "PINE FLAT",
      "SAUCER",
      "SPRING HILL",
      "WALD"
  };

}
