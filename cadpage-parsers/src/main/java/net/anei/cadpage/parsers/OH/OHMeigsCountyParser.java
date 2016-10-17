package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;



public class OHMeigsCountyParser extends DispatchEmergitechParser {
  
  public OHMeigsCountyParser() {
    super(CITY_LIST, "MEIGS COUNTY", "OH", TrailAddrType.INFO);
  }

  private static final String[] CITY_LIST = new String[]{
    
      //Villages

      "MIDDLEPORT",
      "POMEROY",
      "RACINE",
      "RUTLAND",
      "SYRACUSE",

      //Townships

      "BEDFORD",
      "CHESTER",
      "COLUMBIA",
      "LEBANON",
      "LETART",
      "OLIVE",
      "ORANGE",
      "RUTLAND",
      "SALEM",
      "SALISBURY",
      "SCIPIO",
      "SUTTON",

      //Unincorporated communities

      "APPLE GROVE",
      "CHESTER",
      "DARWIN",
      "LANGSVILLE",
      "LONG BOTTOM",
      "PORTLAND",
      "REEDSVILLE",
      "TUPPERS PLAINS",
      
      // Athens County
      "ALBANY",
      "SHADE",
      "COOLVILLE"
  };
}
