package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCBladenCountyParser extends DispatchSouthernParser {

  public NCBladenCountyParser() {
    super(CITY_LIST, "BLADEN COUNTY", "NC",
          DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_CODE | DSFLG_TIME);
  }

  @Override
  public String getFilter() {
    return "@bladenco.org";
  }

  private static final String[] CITY_LIST = new String[]{

    // Towns
    "BLADENBORO",
    "CLARKTON",
    "DUBLIN",
    "EAST ARCADIA",
    "ELIZABETHTOWN",
    "TAR HEEL",
    "WHITE LAKE",

    // Census-designated places
    "BUTTERS",
    "KELLY",
    "WHITE OAK",

    // Unincorporated communities
    "ABBOTTSBURG",
    "AMMON",
    "AMMON FORD",
    "COLLY TOWNSHIP",
    "COUNCIL",
    "ROSINDALE",

    // Townships
    "ABBOTTSBURG",
    "BETHEL",
    "BLADENBORO",
    "BROWN MARSH",
    "CARVERS CREEK",
    "CENTRAL",
    "CLARKTON",
    "COLLY",
    "CYPRESS CREEK",
    "ELIZABETHTOWN",
    "EAST ARCADIA",
    "FRENCHES CREEK",
    "HOLLOW",
    "LAKE CREEK",
    "TARHEEL",
    "TURNBULL",
    "WHITE OAK",
    "DUBLIN",
    "WHITES CREEK"
  };
}
