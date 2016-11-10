package net.anei.cadpage.parsers.CT;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class CTHartfordCountyFarmingtonParser extends CTNewHavenCountyBParser {
  
  public CTHartfordCountyFarmingtonParser() {
    super(CITY_LIST, CITY_CODES, "HARTFORD COUNTY", "CT");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSpecialStreets(
        "FOX GLEN",
        "NEW HARTFORD TOWN LINE"
    );
  }
  
  @Override
  public String getFilter() {
    return "paging@rockyhillct.gov,pdpaging@farmington-ct.org,dispatch@westhartford.org,FirePaging@TownofCantonCT.org";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strCross.startsWith("(CATI)")) {
      data.strPlace = append(data.strPlace, " ", "(CATI)");
      data.strCross = data.strCross.substring(6).trim();
    }
    return true;
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
    "BAHRE CORNER",
    "BATTERSON PARK",
    "BEACON HILL",
    "BEL AIRE",
    "BIDWELL FARM",
    "BIRCH KNOLL",
    "BLISS MEMORIAL",
    "BOULDER RIDGE",
    "BREEZY HILL",
    "BUENA VISTA",
    "BUNKER HILL",
    "BURNT HILL",
    "CANTON SPRINGS",
    "CENTURY HILLS",
    "CIDER MILL",
    "COLD SPRING",
    "COMMERCE CENTER",
    "COPE FARMS",
    "COPPER BEECH",
    "EAST GATE",
    "EAST HILL",
    "EAST SHORE",
    "ELM COMMONS",
    "FARM GLEN",
    "FARMINGTON CHASE",
    "FAWN HILL",
    "FIELD STONE",
    "FOREST HILLS",
    "FOX HILL",
    "GEO WASHINGTON",
    "GEORGE WASHINGTON",
    "GREAT MEADOW",
    "HALF ACRE",
    "HALF KING",
    "HANG DOG",
    "HART RIDGE",
    "HEMLOCK NOTCH",
    "HIDDEN OAK",
    "HIGH HILL",
    "HIGH RIDGE",
    "HIGHWOOD CROSSING",
    "HINMAN MEADOW",
    "HUNTERS CROSSING BARNES HILL",
    "INDIAN HILL",
    "JOHNNYCAKE MTN",
    "KATHERINE GAYLORD",
    "KROL FARM",
    "LAKE GARDA",
    "LITTLE OAK",
    "LITTLEBROOK CROSSING",
    "MOUNTAIN SPRING",
    "MOUNTAIN TOP PASS",
    "MOUNTAIN VIEW",
    "NORTH MAIN",
    "NORTH MOUNTAIN",
    "PAUL SPRING",
    "POLLY DAN",
    "POND VIEW",
    "POPLAR HILL",
    "PUNCH BROOK",
    "QUEEN'S PEAK",
    "RED OAK HILL",
    "SAW MILL",
    "SCHOOL HOUSE",
    "SCOTT SWAMP",
    "SECRET LAKE",
    "SHINGLE MILL",
    "SILAS DEANE",
    "SILVERMINE ACRES",
    "SOUTH MAIN",
    "SOUTH QUAKER",
    "SOUTH RIDGE",
    "SPENO RDG FRANCE",
    "STONE HILL",
    "STONY HILL",
    "TALCOTT NOTCH",
    "THE MEWS CENTURY HILLS",
    "TIMBER BROOK",
    "TOWN FARM",
    "TOWN LINE",
    "TROUT BROOK",
    "TUNXIS VILLAGE",
    "TWO MILE",
    "WEST CHIPPEN HILL",
    "WEST DISTRICT",
    "WEST GATE",
    "WEST MOUNTAIN",
    "WEST SIDE",
    "WOLF PIT",
    "WOODCHUCK HILL",
    "ZONE 275 SMOKE CARILLON"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT - MV INJ ALPHA",
      "ACCIDENT - MV INJ BRAVO",
      "ACCIDENT- MV INJ DELTA",
      "ASSIST CITIZEN",
      "ASSIST L. E. AGENCY",
      "ALARM - FIRE",
      "ALARM - MEDICAL",
      "BOX ALARM",
      "C.O. ALARM",
      "CELLAR PUMP/WATER EMERGENCY",
      "EMS - MUTUAL AID",
      "EMS ASSIST",
      "FIRE - BRUSH FIRE",
      "FIRE - CO ALARM",
      "FIRE - CO ALARM W/ NO ILL EFFECTS",
      "FIRE - EMS MUTUAL AID",
      "FIRE - GENERAL ALARM",
      "FIRE - HAZMAT",
      "FIRE - MISCELLANEOUS",
      "FIRE - MUTUAL AID",
      "FIRE - MV",
      "FIRE - ODOR OF GAS/GAS LEAK",
      "FIRE - OTHER",
      "FIRE - SMOKE INVESTIGATION (IN BUILDING)",
      "FIRE - SMOKE/GAS INVEST INSIDE",
      "FIRE - SMOKE/GAS INVEST OUTSIDE",
      "FIRE - STRUCTURE FIRE",
      "FIRE - WATER RESCUE",
      "FIRE ALARM",
      "FLUID SPILL",
      "HAZARDOUS CONDITIONS",
      "HAZMAT - CHARLIE RESPONSE",
      "MEDICAL CALL ALPHA RESPONSE",
      "MEDICAL CALL BRAVO RESPONSE",
      "MEDICAL CALL CHARLIE RESPONSE",
      "MEDICAL CALL DELTA RESPONSE",
      "MEDICAL CALL ECHO RESPONSE",
      "MEDICAL COMPLAINT",
      "MISCELLANEOUS OFFICER",
      "MUTUAL AID ASSIGNMENT",
      "MVA",
      "MVA (INJURIES)",
      "MVA (INJURIES)",
      "ODOR OF NATURAL GAS",
      "ODOR OF SMOKE INDOORS",
      "ODOR OF SMOKE OUTDOORS",
      "RESIDENTIAL LOCKOUT",
      "STILL ALARM",
      "STRUCTURE ALARM",
      "UNKNOWN",
      "VEHICLE FIRE",
      "WATER PROBLEM RESIDENTIAL",
      "WATER RESCUE",
      "WELFARE CHECK",
      "WIRE DOWN"
  );
  
  private static final String[] CITY_LIST = new  String[]{

    // Cities
    "BRISTOL",
    "HARTFORD",
    "NEW BRITAIN",

    // Towns
    "AVON",
    "BERLIN",
    "BLOOMFIELD",
    "BURLINGTON",
    "CANTON",
    "EAST GRANBY",
    "EAST HARTFORD",
    "EAST WINDSOR",
    "ENFIELD",
    "FARMINGTON",
    "GLASTONBURY",
    "GRANBY",
    "HARTLAND",
    "MANCHESTER",
    "MARLBOROUGH",
    "NEWINGTON",
    "PLAINVILLE",
    "ROCKY HILL",
    "SIMSBURY",
    "SOUTH WINDSOR",
    "SOUTHINGTON",
    "SUFFIELD",
    "UNIONVILLE",
    "WEST HARTFORD",
    "WETHERSFIELD",
    "WINDSOR",
    "WINDSOR LOCKS",
    
    // Middlesex County
    "CROMWELL"
  };
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
     "FARM", "FARMINGTON",
     "UNVL", "UNIONVILLE",
     "WLFD", "WALLINGFORD"
  });

}
