package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCNorthamptonCountyCParser extends DispatchSouthernParser {

  public NCNorthamptonCountyCParser() {
    super(CITY_LIST, "NORTHAMPTON COUNTY", "NC",
          DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_X | DSFLG_OPT_CODE | DSFLG_ID | DSFLG_TIME);
  }


  @Override
  public String getFilter() {
    return "ssreports@nhcnc.net";
  }

  private static final String[] CITY_LIST = new String[] {

      // Towns
      "CONWAY",
      "GARYSBURG",
      "GASTON",
      "JACKSON",
      "LASKER",
      "RICH SQUARE",
      "SEABOARD",
      "SEVERN",
      "WOODLAND",

      // Unincorporated communities
      "MARGARETTSVILLE",
      "MILWAUKEE",
      "PLEASANT HILL",
      "TURNERS CROSSROADS",

      // Townships
      "GASTON",
      "JACKSON",
      "KIRBY",
      "OCCONEECHEE",
      "PLEASANT HILL",
      "RICH SQUARE",
      "ROANOKE",
      "SEABOARD",
      "WICCANEE"
  };
}
