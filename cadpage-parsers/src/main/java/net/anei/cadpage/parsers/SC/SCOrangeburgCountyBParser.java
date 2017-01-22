package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchA67Parser;

public class SCOrangeburgCountyBParser extends DispatchA67Parser {
  
  public SCOrangeburgCountyBParser() {
    super(":", CITY_LIST, "ORANGEBURG COUNTY", "SC", A67_OPT_CROSS, null, ".*");
    removeWords("CV");  // CV is a unit
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "ORANGEBURG",

    // Towns
    "BOWMAN",
    "BRANCHVILLE",
    "COPE",
    "CORDOVA",
    "ELLOREE",
    "EUTAWVILLE",
    "HOLLY HILL",
    "LIVINGSTON",
    "NEESES",
    "NORTH",
    "NORWAY",
    "ROWESVILLE",
    "SANTEE",
    "SPRINGFIELD",
    "VANCE",
    "WOODFORD",

    // Census-designated places
    "BROOKDALE",
    "WILKINSON HEIGHTS"
  };

}
