package net.anei.cadpage.parsers.WV;


import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class WVRaleighCountyParser extends DispatchA19Parser {

  public WVRaleighCountyParser() {
    super(CITY_CODES, "RALEIGH COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "Raleigh911@SuddenLinkMail.com,Raleigh911@Raleigh911.org";
  }


  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equals("OUT OF COUNTY")) data.defCity = "";
    return true;
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
      "GLEN MORGAN",        "BEAVER",
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
      "OUT OF COUNTY",      "",
      "PACKSVILLE",         "",
      "PLEASANT HILLS",     "BECKLEY",
      "PLUTO",              "SHADY SPRING",
      "SANDLICK",           "BECKLEY",
      "SKELETON",           "BECKLEY",
      "SOAK CREEK",         "SOPHIA",
      "SPRAGUE",            "BECKLEY",
      "STANAFORD",          "BECKLEY",
      "STOVER",             "GLEN DANIEL",
      "SWEENEYSBURG",       "BECKLEY",
      "TOLLEYTOWN",         "SURVEYOR"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ABH", "ABRAHAM",
      "AME", "AMEAGLE",
      "AMG", "AMIGO",
      "ARN", "ARNETT",
      "ART", "ARTIE",
      "BDY", "BRADLEY",
      "BEA", "BEAVER",
      "BEC", "BECKLEY",
      "BES", "BESOCO",
      "BJN", "BECKLEY JCT",
      "BJY", "BLUE JAY",        // -> BEAVER
      "BLT", "BOLT",
      "BLV", "BLUEMONT",
      "BRG", "BRAGG",
      "BRN", "BARN",
      "BS",  "BERKELEY SPRING",
      "BVV", "BERRYVILLE",
      "CBH", "CABELL HEIGHTS",  // -> BECKLEY
      "CBO", "CRAB ORCHARD",
      "CBY", "CRANBERRY",
      "CCY", "COAL CITY",
      "CLC", "CLEAR CREEK",
      "CLH", "CALLOWAY HGTS",
      "CLR", "COOL RIDGE",
      "CRD", "COLCORD",
      "CRW", "CROW",
      "CT",  "CHARLES TOWN",
      "CTR", "CHARLES TOWN",
      "CTU", "CHARLES TOWN",
      "CVC", "COVE CREEK",
      "CVL", "CIRTSVILLE",
      "DAM", "DAMERON",
      "DAN", "DANIELS",
      "DCK", "DRY CREEK",
      "DHL", "DRY HILL",        // -> BECKLEY
      "DRT", "DOROTHY",
      "DUN", "DUNNS",
      "ECC", "ECCLES",
      "EDW", "EDWIGHT",
      "EGF", "EAST GULF",
      "EGR", "EGERIA",          // -> ODD
      "EUN", "EUNICE",          // -> WHITESVILLE
      "FAY", "FAYETTE COUNTY",
      "FCO", "FIRECO",
      "FDL", "FAIRDALE",
      "FRH", "FARLEY HILL",
      "FTP", "FLAT TOP",
      "FZP", "FITZPATRICK",
      "GDL", "GLEN DANIEL",
      "GHT", "GHENT",
      "GMG", "GLEN MORGAN",     // -> BEAVER
      "GRV", "GRANDVIEW",       // -> BEAVER
      "GVW", "GLEN VIEW",
      "GWT", "GLEN WHITE",
      "HAR", "HARPER",
      "HF",  "HARPERS FERRY",
      "HFB", "HARPERS FERRY",
      "HFU", "HARPERS FERRY",
      "HHT", "HARPER HEIGHTS",  // -> BECKLEY
      "HIN", "HINTON",
      "HKS", "HOTCHKISS",
      "HLN", "HELEN",
      "HPK", "HARPER PARK",     // -> BECKLEY
      "IMT", "IRISH MOUNTAIN",
      "JBN", "JONBEN",
      "JOS", "JOSEPHINE",
      "KAY", "KAYFORD",
      "KIL", "KILLARNEY",
      "KVR", "KEARNEYSVILLE",
      "KVU", "KEARNEYSVILLE",
      "LBK", "LILLYBROOK",      // -> COAL CITY
      "LGO", "LEGO",
      "LRK", "LANARK",
      "LTR", "LESTER",
      "LVL", "LEEVALE",        // -> WHITESVILLE
      "MAB", "MABSCOTT",
      "MAC", "MACARTHUR",
      "MCC", "MCCREERY",
      "MCL", "MONTCOAL",
      "MED", "MEAD",
      "MET", "METALTON",
      "MFK", "MAPLE FORK",      // -> BRADLEY
      "MLC", "MILL CREEK",
      "MNR", "MAYNOR",
      "MPN", "MCALPIN",
      "MTB", "MOUNT TABOR",     // -> BECKLEY
      "MVU", "MILLVILLE",
      "MWY", "MIDWAY",
      "NEW", "NEW",
      "NMA", "NAOMA",
      "ODD", "ODD",
      "OGV", "OAK GROVE",
      "OOC", "OUT OF COUNTY",
      "PBM", "PETTRY BOTTOM",
      "PEM", "PEMBERTON",
      "PHL", "PRICE HILL",
      "PRP", "PROSPERITY",
      "PSN", "PICKSHIN",
      "PTH", "PLEASANT HILLS",
      "PTO", "PLUTO",           // -> SHADY SPRING
      "PTU", "PETTUS",
      "PVL", "PACKSVILLE",
      "PVV", "PURCELLVILLE",
      "PVW", "PINEY VIEW",
      "PWK", "PRINCEWICK",
      "RAL", "RALEIGH COUNTY",
      "RCK", "ROCK CREEK",
      "RHO", "RHODELL",
      "RN",  "RANSON",
      "RNC", "RANSON",
      "RNU", "RANSON",
      "SBY", "STOTESBURY",
      "SCK", "SOAK CREEK",      // -> SOPHIA
      "SDL", "SANDLICK",
      "SFK", "SLAB FORK",
      "SJU", "SHENANDOAH JCT",
      "SKE", "SKELTON",         // -> BECKLEY
      "SPH", "SOPHIA",
      "SPR", "SPRAGUE",         // -> BECKLEY
      "SPU", "SUMMIT POINT",
      "SRV", "SURVEYOR",
      "SSG", "SHADY SPRINGS",
      "ST",  "SHEPHERDSTOWN",
      "STK", "STICKNEY",
      "STN", "STANAFORD",       // -> BECKLEY
      "STU", "SHEPHERDSTOWN",
      "STV", "STOVER",          // -> GLEN DANIEL
      "SUL", "SULLIVAN",
      "SUM", "SUMMERS CTY",
      "SUN", "SUNDIAL",
      "SWB", "SWEENEYSBURG",    // -> BECKLEY
      "TAM", "TAMS",
      "TRY", "TERRY",
      "TTN", "TOLLEYTOWN",
      "URY", "URY",
      "WBT", "WILLIBET",
      "WBY", "WHITBY",
      "WHM", "WICKHAM",
      "WTK", "WHITE OAK",
      "WVL", "WHITESVILLE",
      "WVW", "WESTVIEW"
  });
}