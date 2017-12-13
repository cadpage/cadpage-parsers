package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class GAStephensCountyParser extends DispatchB2Parser {

  public GAStephensCountyParser() {
    super(CITY_LIST, "STEPHENS COUNTY", "GA");
    setupCallList(CALL_TABLE);

  }
 
  private static final String[] CITY_LIST = new String[]{

      "AVALON",
      "EASTANOLLEE",
      "MARTIN",
      "TOCCOA"

  };
  
  private static final CodeSet CALL_TABLE = new CodeSet(
      
      "1078",     "ASSIST OFFICER",
      "ASSIST",   "ASSIST PATIENT",
      "1046P",    "ASSIST PERSON WITH...",
      "BACK",     "BACK PAIN",
      "CARBON",   "CARBON MONOXIDE INHALATION",
      "1080",     "CHASE",
      "CHEST",    "CHEST PAIN",
      "DIABET",   "DIABETIC PROBLEM",
      "DIFFBR",   "DIFFICULTY BREATHING",
      "EQUIP",    "EQUIPMENT MALFUNCTION",
      "1070A",    "FIRE ALARM",
      "FLOOD",    "FLOODED AREA OR ROADWAY",
      "MALARM",   "MEDICAL ALARM",
      "POWER",    "POWER OUTAGE",
      "SEAZUR",   "SEIZURE",
      "SMOKE",    "SMOKE INVESTIGATION",
      "SPILL",    "SPILL OTHER FUEL",
      "1070S",    "STRUCTURE FIRE",
      "1044A",    "SUICIDE  ATTEMPT",
      "1050NI",   "TRAFFIC ACCIDENT - NO INJURIES",
      "1050I",    "TRAFFIC ACCIDENT WITH INJURIES",
      "TREE",     "TREE DOWN",
      "UNCON",    "UNCONSCIOUS SUBJECT",
      "UNRESP",   "UNRESPONSIVE PERSON",
      "1070V",    "VEHICLE FIRE"
      
  );
}
