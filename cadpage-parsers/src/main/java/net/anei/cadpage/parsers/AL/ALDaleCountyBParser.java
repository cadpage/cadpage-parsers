package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ALDaleCountyBParser extends DispatchSouthernParser {

  public ALDaleCountyBParser() {
    super(CITY_LIST, "DALE COUNTY", "AL",
          DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_X | DSFLG_ID | DSFLG_TIME);
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "DALEVILLE",
      "DOTHAN",
      "ENTERPRISE",
      "LEVEL PLAINS",
      "OZARK",

      // Towns
      "ARITON",
      "CLAYHATCHEE",
      "GRIMES",
      "MIDLAND CITY",
      "NAPIER FIELD",
      "NEWTON",
      "PINCKARD",

      // Census-designated place
      "FORT RUCKER",

      // Unincorporated communities
      "ARGUTA",
      "ASBURY",
      "BARNES",
      "BERTHA",
      "CLOPTON",
      "DILLARD",
      "ECHO",
      "EWELL",
      "GERALD",
      "KELLY",
      "MABSON",
      "ROCKY HEAD",
      "SKIPPERVILLE",
      "SYLVAN GROVE"

  };

}
