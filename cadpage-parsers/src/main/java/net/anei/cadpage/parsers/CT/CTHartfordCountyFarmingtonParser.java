package net.anei.cadpage.parsers.CT;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeSet;


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
    return "pdpaging@farmington-ct.org,paging@cantonfireandems.org";
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT - MV INJ ALPHA",
      "ACCIDENT - MV INJ BRAVO",
      "ACCIDENT- MV INJ DELTA",
      "ALARM - FIRE",
      "ALARM - MEDICAL",
      "BOX ALARM",
      "CELLAR PUMP/WATER EMERGENCY",
      "EMS - MUTUAL AID",
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
      "FIRE - SMOKE/GAS INVEST INSIDE",
      "FIRE - SMOKE/GAS INVEST OUTSIDE",
      "FIRE - STRUCTURE FIRE",
      "FIRE - WATER RESCUE",
      "HAZMAT - CHARLIE RESPONSE",
      "MEDICAL CALL ALPHA RESPONSE",
      "MEDICAL CALL BRAVO RESPONSE",
      "MEDICAL CALL CHARLIE RESPONSE",
      "MEDICAL CALL DELTA RESPONSE",
      "MEDICAL CALL ECHO RESPONSE",
      "Medical Complaint",
      "MUTUAL AID ASSIGNMENT",
      "MVA",
      "MVA (INJURIES)",
      "MVA (INJURIES)",
      "STILL ALARM",
      "STRUCTURE ALARM",
      "WATER RESCUE"
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
    "WINDSOR LOCKS"
  };
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
     "FARM", "FARMINGTON",
     "UNVL", "UNIONVILLE",
     "WLFD", "WALLINGFORD"
  });

}
