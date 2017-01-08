package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class KYKnoxCountyParser extends DispatchB2Parser {
  
  public KYKnoxCountyParser() {
    super("BC911:", CITY_LIST, "BOYD COUNTY", "KY");
    setupCallList(CALL_LIST);

  }
 
  
  private static final String[] CITY_LIST = new String[]{

      "ARTEMUS",
      "BARBOURVILLE",
      "CORBIN",
      "FLATLICK",
      "GRAY",
      "NORTH CORBIN",
      "WOODBINE"

  };
  
  private CodeSet CALL_LIST = new CodeSet(
      
      "ACCIDENT W ENTRAP W INJURIES",
      "ACCIDENT WITH INJURIES",
      "BRUSH FIRE",
      "FALL",
      "FIRE ALARM",
      "FIRE MISCELLANEOUS",
      "GAS LEAK",
      "PAIN",
      "SEIZURE",
      "STRUCTURE FIRE",
      "VEHICLE FIRE"

  );
}
