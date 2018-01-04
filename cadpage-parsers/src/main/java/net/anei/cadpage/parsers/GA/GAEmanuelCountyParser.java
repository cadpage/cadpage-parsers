package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class GAEmanuelCountyParser extends DispatchB2Parser {

  public GAEmanuelCountyParser() {
    super("EMANUEL COUNTY E911:||EMANUELCOE911:", CITY_LIST, "EMANUEL COUNTY", "GA");
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
      "FANNIE BREWER",
      "FRIENDSHIP CHURCH",
      "GRANDE CREEK",
      "GRIFFIN FERRY",
      "HALLS BRIDGE",
      "HAWHAMMOCK CHURCH",
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
      "NOONDAY CHURCH",
      "NUNEZ LEXSY",
      "OAK GROVE CHURCH",
      "OAK PARK",
      "PARK CIRCLE",
      "ROUNTREE FARM",
      "ROY WOODS",
      "SAM OVERSTREET",
      "ST GALILEE",
      "TONY BAKER",
      "WADLEY COLEMAN LAKE",
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "911 HANG UP",
      "ABDOMINAL PAIN",
      "ALTERED LEVEL OF CONSCIOUSNESS",
      "ALTERED MENTAL STATUS",
      "ARMED ROBBERY",
      "ARRESTED SUBJECT",
      "BANK ALARM",
      "BOMB THREAT",
      "BROKE DOWN VEHICLE",
      "BURGLARY REPORT",
      "CAD CALL CREATED IN ERROR",
      "CHASE / VEHICLE OR FOOT",
      "CHEST PAINS",
      "CRIME IN PROGRESS",
      "CUSTODY DISPUTE",
      "DEAD ON ARRIVAL",
      "DIABETIC EMERGENCY",
      "DIFFICULTY BREATHING",
      "DOMESTIC IN PROGRESS",
      "DOMESTIC PREVIOUSLY OCCURRED",
      "FIGHT",
      "FIRE ALARM",
      "FIRE - BRUSH",
      "FIRE - EXPLOSION",
      "FIRE -EXPLOSION",
      "FIRE - RUBBISH",
      "FIRE-SMOKE",
      "FIRE TRANSFORMER",
      "FIRE - UNKNOWN TYPE",
      "GAS LEAK",
      "GAS SPILL",
      "GENERAL ALARM",
      "GENERAL WEAKNESS",
      "GUN SHOT WOUND",
      "HAZMAT INCIDENT",
      "HELICOPTER LANDING ZONE",
      "HIT AND RUN",
      "INTERFACILITY TRANSFER",
      "INTOXICATED PEDESTRIAN",
      "INVESTIGATION",
      "IRATE SUBJECT",
      "LIFT ASSISTANCE",
      "MEDICAL CALL",
      "MISC CALL",
      "MISSING JUVENILE",
      "MISSING PERSON",
      "MURDER/HOMICIDE",
      "MVA",
      "MVA ROLLOVER",
      "MVA WITH INJURIES",
      "NAUSEA AND VOMITING",
      "NON EMERGENCY TRANSFER",
      "NON EMERGENCY TRANSPORT",
      "OFFICER ASSIST",
      "POSSIBLE OVERDOSE",
      "POWER LINE DOWN",
      "PREG COMPLICATIONS/OB/GYN",
      "PREG COMPLICATONS/OB/GYN",
      "PRISON / JAIL BREAK",
      "PROWLER",
      "RECKLESS DRIVER",
      "REPORT",
      "RI0T",
      "RUNAWAY",
      "SEIZURE",
      "SERVE PAPER",
      "SHOPLIFTING",
      "SHOTS FIRED",
      "SPEEDING",
      "STABBING",
      "STROKE",
      "STRUCTURE FIRE",
      "SUICIDE",
      "SUICIDE ATTEMP",
      "SUICIDE ATTEMPT",
      "SUSPICIOUS PERSON",
      "TEST CALL",
      "THEFT",
      "TRAFFIC STOP",
      "TRANSPORT",
      "TRAUMA",
      "UNRULY JUVENILE",
      "UNWANTED PERSON",
      "VEHICLE DAMAGE",
      "VEHICLE FIRE",
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
      "VIDALIA"
  };
}
