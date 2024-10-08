package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class KYCaldwellCountyAParser extends DispatchEmergitechParser {
  
  public KYCaldwellCountyAParser() {
    super(CITY_LIST, "CALDWELL COUNTY", "KY", EMG_FLG_NO_PLACE, TrailAddrType.NONE);
  }

  private static final String[] CITY_LIST = new String[]{
      
      // Cities
      "FREDONIA",
      "PRINCETON",

      // Unincorporated communities
      "BAKERS",
      "BALDWIN FORD",
      "BLACK HAWK",
      "THE BLUFF",
      "CEDAR BLUFF",
      "CLAXTON",
      "COBB",
      "CRESSWELL",
      "CRIDER",
      "CROWTOWN",
      "ENON",
      "FARMERSVILLE",
      "FLAT ROCK",
      "FRIENDSHIP",
      "FRYER",
      "HARPER FORD",
      "HOPSON",
      "LAKE SHORE",
      "LEWISTOWN",
      "MCGOWAN",
      "MIDWAY",
      "NEEDMORE",
      "OTTER POND",
      "PUMPKIN CENTER",
      "QUINN",
      "RUFUS",
      "SHADY GROVE",
      "SMITH FORD",
      "TOM GRAY FORD",
      "WHITE SULPHUR"
  };
}
