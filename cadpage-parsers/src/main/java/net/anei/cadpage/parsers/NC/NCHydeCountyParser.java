package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class NCHydeCountyParser extends DispatchB2Parser {

  public NCHydeCountyParser() {
    super("911-CENTER:", CITY_LIST, "HYDE COUNTY", "NC", B2_FORCE_CALL_CODE);
    setupCallList(CALL_LIST);
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      
      "BRUSH / WOODS / GRASS FIRE",
      "CARDIAC/CHEST PAINS",
      "DIABETIC EMERGENCY",
      "EMS UNKNOWN",
      "FALL",
      "FIRE ALARM",
      "FUEL OR GAS SPILL",
      "HELICOPTER LANDING",
      "INJURED PERSON",
      "RESPIRATORY DISTRESS",
      "SICK CALL",
      "SUBJECT HEMORRHAGING",
      "SUICIDE"
 
  );
  static final String[] CITY_LIST = new String[]{
      
      "CURRITUCK",
      "ENGELHARD",
      "FAIRFIELD",
      "GERMANTOWN",
      "LAKE LANDING",
      "LAST CHANCE",
      "MATTAMUSKEET",
      "NEBRASKA",
      "OCRACOKE",
      "SCRANTON",
      "SWAN QUARTER",
      
      // Beafort County
      "BELHAVEN",
      "PANTEGO"
      
  };

}
