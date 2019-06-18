package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.dispatch.DispatchChiefPagingParser;

public class MDCarolineCountyAParser extends DispatchChiefPagingParser {
  
  public MDCarolineCountyAParser() {
    super(CITY_LIST, "CAROLINE COUNTY", "MD");
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "DENTON",
    "FEDERALSBURG",
    "GOLDSBORO",
    "GREENSBORO",
    "HENDERSON",
    "HILLSBORO",
    "MARYDEL",
    "PRESTON",
    "RIDGELY",
    "TEMPLEVILLE",

    // Towns
    "CHOPTANK",
    "WEST DENTON",
    "WILLISTON",

    // Unincorporated Communities
    "AMERICAN CORNER",
    "ANDERSONTOWN",
    "BALTIMORE CORNER",
    "BETHLEHEM",
    "BURRSVILLE",
    "GILPIN POINT",
    "GROVE",
    "HARMONY",
    "HICKMAN",
    "HOBBS",
    "JUMPTOWN",
    "LINCHESTER",
    "OAKLAND",
    "OIL CITY",
    "TANYARD",
    "TWO JOHNS",
    "RELIANCE",
    "WHITELEYSBURG"
    
  };
}
