package net.anei.cadpage.parsers.WV;


import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class WVRaleighCountyParser extends DispatchA19Parser {
  
  public WVRaleighCountyParser() {
    super(CITY_CODES, "RALEIGH COUNTY", "WV");
  }
  
  @Override
  public String getFilter() {
    return "Raleigh911@SuddenLinkMail.com";
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, CITY_PLACE_NAMES);
  }
  
  private static final Properties CITY_PLACE_NAMES = buildCodeTable(new String[]{
      "BLUE JAY",           "BEAVER",
      "CABELL HEIGHTS",     "BECKLEY",
      "CALLOWAY HEIGHTS",   "BECKLEY",
      "CRANBERRY",          "BECKLEY",
      "COOL RIDGE",         "SHADY SPRING",
      "DAMERON",            "",
      "DRY HILL",           "BECKLEY",
      "EGERIA",             "ODD",
      "EUNICE",             "WHITESVILLE",
      "FARLEY HILL",        "PRINCEWICK",
      "FITZPATRICK",        "BECKLEY",
      "GLEN VIEW",          "CRAB ORCHARD",
      "GRANDVIEW",          "BEAVER",
      "HARPER",             "BECKLEY",
      "HARPER HEIGHTS",     "BECKLEY",
      "HARPER PARK",        "BECKLEY",
      "LEEVALE",            "WHITESVILLE",
      "LILLYBROOK",         "COAL CITY",
      "MAPLE FORK",         "BRADLEY",
      "MCCREERY",           "BECKLEY",
      "MCREERY",            "BECKLEY",
      "METALTON",           "BECKLEY",
      "MOUNT TABOR",        "BECKLEY",
      "OAK GROVE",          "BECKLEY",
      "OOC",                "",
      "PACKSVILLE",         "",
      "PLEASANT HILLS",     "BECKLEY",
      "PLUTO",              "SHADY SPRING",
      "SANDLICK",           "BECKLEY",
      "SPRAGUE",            "BECKLEY",
      "STANAFORD",          "BECKLEY",
      "STOVER",             "GLEN DANIEL",
      "SWEENEYSBURG",       "BECKLEY",
      "TOLLEYTOWN",         "SURVEYOR"
  });
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AMG", "AMIGO",
      "BDY", "BRADLEY",
      "BEA", "BEAVER",
      "BEC", "BECKLEY",
      "BJY", "BLUE JAY",        // -> BEAVER
      "BRG", "BRAGG",
      "CBO", "CRAB ORCHARD",
      "CBH", "CABELL HEIGHTS",  // -> BECKLEY
      "CCY", "COAL CITY",
      "CLR", "COOL RIDGE",
      "CRD", "COLCORD",
      "CRW", "CROW",
      "DAN", "DANIELS",
      "DHL", "DRY HILL",        // -> BECKLEY
      "EGR", "EGERIA",          // -> ODD
      "FAY", "FAYETTE COUNTY",
      "FDL", "FAIRDALE",
      "FTP", "FLAT TOP",
      "FZP", "FITZPATRICK",
      "GDL", "GLEN DANIEL",
      "GHT", "GHENT",
      "GMG", "BEAVER",          // ????
      "GRV", "GRANDVIEW",       // -> BEAVER
      "GWT", "GLEN WHITE",
      "HHT", "HARPER HEIGHTS",  // -> BECKLEY
      "HPK", "HARPER PARK",     // -> BECKLEY
      "LBK", "LILLYBROOK",      // -> COAL CITY
      "LTR", "LESTER",
      "MAB", "MABSCOTT",
      "MAC", "MACARTHUR",
      "MFK", "MAPLE FORK",      // -> BRADLEY
      "MTB", "MOUNT TABOR",     // -> BECKLEY
      "NMA", "NAOMA",
      "PEM", "PEMBERTON",
      "PWK", "PRINCEWICK",
      "PRP", "PROSPERITY",
      "PTO", "PLUTO",           // -> SHADY SPRING
      "PVW", "PINEY VIEW",
      "RAL", "RALEIGH COUNTY",
      "RHO", "RHODELL",
      "SBY", "STOTESBURY",
      "SCK", "SOPHIA",          // ???
      "SKE", "BECKLEY",         // ???
      "SPH", "SOPHIA",
      "SPR", "SPRAGUE",         // -> BECKLEY
      "SRV", "SURVEYOR",
      "SSG", "SHADY SPRINGS",
      "SWB", "SWEENEYSBURG",    // -> BECKLEY
      "STN", "STANAFORD",       // -> BECKLEY
      "URY", "URY",
      "WTK", "WHITE OAK"
  });
}