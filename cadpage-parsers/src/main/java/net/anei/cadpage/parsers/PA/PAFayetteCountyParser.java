package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

/**
 * Fayette County, Pennsylvania
 */
public class PAFayetteCountyParser extends DispatchB2Parser {

  public PAFayetteCountyParser() {
    super("FAYETTE-911:", CITY_LIST, "FAYETTE COUNTY", "PA");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "FAYETTE-911@fcema.org";
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "QUAIL HILL",
      "ROUND BARN",
      "TOWN COUNTRY",
      "VANCES MILL"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "AED CARDIAC ARREST ALS",
      "AED UNRESPONSIVE ALS",
      "AUTOMATIC FIRE ALARM COMERCIAL",
      "E_BREATHING ALS",
      "E FALLS ALS",
      "E SICK/UNKNOWN ALS",
      "E STROKE ALS",
      "E TRAUMA ALS",
      "F_ACCIDENT NO INJURIES",
      "F-ACCIDENT W/ INJURIES",
      "F_ASSIST",
      "F_STRUCTURE FIRE",
      "F_STRUCTURE FIRE COMMERCIAL",
      "F_TRAFFIC CONTROL",
      "F_TREE DOWN",
      "F_TREE DOWN BUENA",
      "F_UNKNOWN FIRE"
  );
  
  private static final String[] CITY_LIST = new String[]{
      "CONNELLSVILLE",
      "UNIONTOWN",
  
      "BELLE VERNON",
      "BROWNSVILLE",
      "DAWSON",
      "DUNBAR",
      "EVERSON",
      "FAIRCHANCE",
      "FAYETTE CITY",
      "MARKLEYSBURG",
      "MASONTOWN",
      "NEWELL",
      "OHIOPYLE",
      "PERRYOPOLIS",
      "POINT MARION",
      "SEVEN SPRINGS",
      "SMITHFIELD",
      "SOUTH CONNELLSVILLE",
      "VANDERBILT",
    
      "BROWNSVILLE TWP",
      "BULLSKIN",
      "CONNELLSVILLE TWP",
      "DUNBAR TWP",
      "FRANKLIN",
      "GEORGES",
      "GERMAN",
      "HENRY CLAY",
      "JEFFERSON",
      "LOWER TYRONE",
      "LUZERNE",
      "MENALLEN",
      "NICHOLSON",
      "NORTH UNION",
      "PERRY",
      "REDSTONE",
      "SALTLICK",
      "SOUTH UNION",
      "SPRINGFIELD",
      "SPRINGHILL",
      "STEWART",
      "UPPER TYRONE",
      "WASHINGTON",
      "WHARTON"
  };
}
