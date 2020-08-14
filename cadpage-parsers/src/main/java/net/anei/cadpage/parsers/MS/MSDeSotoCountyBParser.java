package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class MSDeSotoCountyBParser extends DispatchA48Parser {

  public MSDeSotoCountyBParser() {
    super(CITY_LIST, "DESOTO COUNTY", "MS", FieldType.X, A48_NO_CODE);
  }

  static final String[] CITY_LIST = new String[]{

    // Cities
    "HERNANDO",
    "HORN LAKE",
    "OLIVE BRANCH",
    "SOUTHAVEN",

    // Towns
    "WALLS",

    // Villages
    "MEMPHIS",

    // Census-designated places
    "BRIDGETOWN",
    "LYNCHBURG",

    // Unincorporated communities
    "COCKRUM",
    "DAYS",
    "EUDORA",
    "LAKE CORMORANT",
    "LAKE VIEW",
    "LEWISBURG",
    "LOVE",
    "MINERAL WELLS",
    "NESBIT",
    "NORFOLK",
    "PLEASANT HILL",
    "WEST DAYS",

    // Marshall County
    "BYHALIA",

    // Tate County
    "INDEPENDENCE"
  };
}
