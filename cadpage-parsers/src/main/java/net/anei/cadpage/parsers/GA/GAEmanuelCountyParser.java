package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class GAEmanuelCountyParser extends DispatchB2Parser {

  public GAEmanuelCountyParser() {
    super("EMANUEL COUNTY E911:", CITY_LIST, "EMANUEL COUNTY", "GA");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "tehanners@gmail.com";
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "BIRD FLANDERS",
      "CANOOCHEE GARFIELD",
      "DELLWOOD MAIN",
      "GRIFFIN FERRY",
      "HALLS BRIDGE",
      "KEAS OLD MILL POND",
      "KING CIRCLE",
      "LAMBS BRIDGE",
      "LONG BAY",
      "MARTIN LUTHER KING",
      "MARY ANN",
      "MCLEOD BRIDGE",
      "MEDICAL CENTER",
      "MOORES FERRY",
      "MT OLIVET CHURCH",
      "MT SHADY CHURCH",
      "NUNEZ LEXSY",
      "OAK PARK",
      "PARK CIRCLE",
      "ROUNTREE FARM",
      "ROY WOODS",
      "SAM OVERSTREET",
      "ST GALILEE",
      "TONY BAKER",
      "WADLEY COLEMAN LAKE"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "911 HANG UP",
      "ABDOMINAL PAIN",
      "ALTERED LEVEL OF CONSCIOUSNESS",
      "ALTERED MENTAL STATUS",
      "BOMB THREAT",
      "BROKE DOWN VEHICLE",
      "BURGLARY REPORT",
      "CAD CALL CREATED IN ERROR",
      "CHEST PAINS",
      "DEAD ON ARRIVAL",
      "DIABETIC EMERGENCY",
      "DIFFICULTY BREATHING",
      "DOMESTIC IN PROGRESS",
      "DOMESTIC PREVIOUSLY OCCURRED",
      "FIGHT",
      "FIRE - UNKNOWN TYPE",
      "FIRE -EXPLOSION",
      "FIRE ALARM",
      "FIRE-SMOKE",
      "GENERAL ALARM",
      "GENERAL WEAKNESS",
      "GUN SHOT WOUND",
      "HAZMAT INCIDENT",
      "HIT AND RUN",
      "INTERFACILITY TRANSFER",
      "INTOXICATED PEDESTRIAN",
      "IRATE SUBJECT",
      "LIFT ASSISTANCE",
      "MEDICAL CALL",
      "MISC CALL",
      "MISSING JUVENILE",
      "MURDER/HOMICIDE",
      "MVA",
      "MVA ROLLOVER",
      "MVA WITH INJURIES",
      "NAUSEA AND VOMITING",
      "NON EMERGENCY TRANSFER",
      "NON EMERGENCY TRANSPORT",
      "OFFICER ASSIST",
      "POSSIBLE OVERDOSE",
      "PREG COMPLICATIONS/OB/GYN",
      "RECKLESS DRIVER",
      "REPORT",
      "SEIZURE",
      "SHOTS FIRED",
      "STABBING",
      "STROKE",
      "STRUCTURE FIRE",
      "SUICIDE",
      "SUICIDE ATTEMPT",
      "SUSPICIOUS PERSON",
      "TEST CALL",
      "THEFT",
      "TRAFFIC STOP",
      "TRANSPORT",
      "TRAUMA",
      "WELFARE CHECK"
  );
  
  
  private static final String[] CITY_LIST = new String[]{

      //cities
      "ADRIAN",
      "GARFIELD",
      "NUNEZ",
      "OAK PARK",
      "STILLMORE",
      "SUMMERTOWN",
      "SWAINSBORO",
      "TWIN CITY",

      //other
      "BLUNDALE",
      "DELLWOOD",
      "CANOOCHEE",
      "NORRISTOWN",
      
      // Burke County
      "MIDVILLE",
      
      // Chandler County
      "METTER",
      
      // Johnson County
      "KITE",
      
      // Toombs County
      "LYONS",
      "VISALIA"
  };
}
