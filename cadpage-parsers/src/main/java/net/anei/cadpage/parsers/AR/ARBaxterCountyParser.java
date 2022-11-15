package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ARBaxterCountyParser extends DispatchSouthernParser {

  public ARBaxterCountyParser() {
    super(CITY_LIST, "BAXTER COUNTY", "AR",
          DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_X | DSFLG_NAME | DSFLG_PHONE | DSFLG_ID | DSFLG_TIME );
  }

  private static final String[] CITY_LIST = new String[] {

      "BAXTER COUNTY",

      // Cities
      "BRIARCLIFF",
      "COTTER",
      "GASSVILLE",
      "LAKEVIEW",
      "MOUNTAIN HOME",
      "NORFORK",
      "SALESVILLE",

      // Town
      "BIG FLAT",

      // Census designated places
      "BUFFALO CITY",
      "GAMALIEL",
      "HENDERSON",
      "MIDWAY",

      // Other unincorporated communities
      "BUFORD",
      "CLARKRIDGE"

  };

}
