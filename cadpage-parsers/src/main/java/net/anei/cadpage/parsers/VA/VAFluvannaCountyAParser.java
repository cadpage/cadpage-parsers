package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;

/**
 * Franklin County, VA
 */
public class VAFluvannaCountyAParser extends DispatchDAPROParser {
  
  public VAFluvannaCountyAParser() {
    super("FLUVANNA COUNTY", "VA");  
    setupCallList(CALL_SET);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "@c-msg.net,cad2@acuecc.org";
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "BUNKER BOULEVA",
    "CEDAR HILL",
    "COMMUNITY HOUSE",
    "DEEP CREEK",
    "DEER PATH",
    "GRAVEL HILL",
    "HADEN MARTIN",
    "HARDWARE HILLS",
    "JAMES MADISON HIGH",
    "KIDDS DAIRY",
    "LITTLE CREEK",
    "LONE OAK",
    "MILES JACKSON",
    "OAK GROVE",
    "RISING SUN",
    "RIVANNA WOODS",
    "SAINT JAMES",
    "SCLATERS FORD",
    "SOUTH BOSTON",
    "STAGE JUNCTION",
    "THOMAS JEFFERSON",
    "UNION MILLS",
    "WAY STATION",
    "ZION STATION"
  };
  
  private static final CodeSet CALL_SET = new CodeSet(
    "ASSAULT AND BATTERY",
    "BREATHING DIFFICULTY",
    "BRUSH FIRE",
    "CARDIAC (WITH CARDIAC HISTORY)",
    "CHEST PAIN (NO CARDIAC HISTORY",
    "DIABETIC ILLNESS/INSULIN REACT",
    "DIZZINESS/VERTIGO/WEAKNESS",
    "FALL/FRACTURE",
    "FIRE ALARM ACTIVATION",
    "HEADACHE",
    "HEMMORHAGE/BLEEDING",
    "LIFT ASSIST",
    "LOG FOR RECORD",
    "MEDICAL ALARM",
    "MOTOR VEHICLE ACCIDENT",
    "MUTUAL AID",
    "NAUSEA/VOMITING",
    "OTHER - EXPLAIN IN REMARKS",
    "PAIN",
    "SICK (UNKNOWN MEDICAL)",
    "SMOKE/ODOR INV INSIDE STRUCTUR",
    "SMOKE/ODOR INV OUTSIDE STRUCTU",
    "STROKE",
    "RUNAWAY",
    "TREE ON LINE/ROAD",
    "UNCONSCIOUS",
    "UNRESPONSIVE",
    "VEHICLE FIRE"
  );
}