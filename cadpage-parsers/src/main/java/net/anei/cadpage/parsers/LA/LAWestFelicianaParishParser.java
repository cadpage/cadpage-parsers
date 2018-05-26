package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class LAWestFelicianaParishParser extends DispatchB2Parser {
		
  public LAWestFelicianaParishParser() {
    super("911CENTER:", CITY_LIST, "WEST FELICIANA PARISH", "LA", B2_OPT_CALL_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSaintNames("FRANCISVILLE");
  }
  
  
  @Override
  public String getFilter() {
    return "911CENTER@wfpso.org";
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "BAINS RISTROPH",
      "CLUBHOUSE WAY",
      "DANIEL PORCHE",
      "DEER RUN",
      "FOREST GLEN",
      "INDIAN MOUND",
      "JONES VAUGHN CREEK",
      "JONES VAUGN CREEK",
      "KIRKS CROSSING",
      "L B HILL",
      "LA PETITE",
      "LAKE HILLS",
      "LAUREL HILL",
      "LIVE OAK",
      "LOW WATER BRIDGE",
      "MCNEAL PARK",
      "MELINDA LEE",
      "MULBERRY HILL",
      "MYRTLE HILL",
      "OAK GROVE",
      "RIVER BEND ACCESS",
      "ROBERT BAILEY",
      "SAGE HILL",
      "SHADY GROVE",
      "ST MARYS",
      "STAR HILL",
      "THOMPSON COVE",
      "WEST FELICIANA"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "911 HANG UP / OPEN LINE",
      "ABDOMINAL/BACK PAIN",
      "ACCIDENT INJURIES UNKNOWN",
      "ACCIDENT WITH INJURIES",
      "AIRPLANE CRASH",
      "ANIMAL/HUMAN BITE",
      "AUTO ACCIDENT NO INJURIES",
      "BLEEDING",
      "BURN PATIENT",
      "CHEST PAINS",
      "CHOKING PATIENT",
      "CPR IN PROGRESS",
      "DIABETIC PATIENT",
      "DIFFICULTY BREATHING",
      "ELECTRIC POLE/ LINE FIRE",
      "FALL",
      "FIGHT",
      "FIRE ALARM",
      "FIRE CALL",
      "GRASS FIRE CALL",
      "GRASS FIRE",
      "HAZARDOUS MATERIALS CALL",
      "HIT AND RUN",
      "MEDICAL ALARM",
      "MEDICAL CALL",
      "MEDICAL CALL",
      "MICELLANEOUS CALL FOR SERVICE",
      "MVA NO INJURIES",
      "MVA UNKNOWN INJURIES",
      "MVA WITH INJURIES",
      "POSSIBLE DEATH",
      "POSSIBLE DROWNING",
      "PREGNANCY/LABOR CALL",
      "PUBLIC ASSIST",
      "REPORT OF FALLEN SUBJECT",
      "REPORT OF SIGHT/SMELL SMOKE, NO FLAMES",
      "REPORT OF SMOKE, NO FLAMES",
      "REPORT OF TRAUMA",
      "REPORTED PAIN",
      "REPORTED SEIZURE",
      "REPORTED SUICIDE",
      "REQUEST TO SEE OFFICER",
      "SEIZURE",
      "SICK PERSON",
      "SILENT ALARM",
      "STROKE",
      "STROKE/CVA",
      "STRUCTURE FIRE",
      "SUSPICIOUS PERSON",
      "TEST",
      "TRASH FIRE",
      "TREE IN ROADWAY",
      "UNCONSCIOUS PATIENT",
      "UNCONSCIOUS SUBJECT",
      "UNKNOWN EXPLOSION",
      "VEHICLE FIRE"
  );

  private static final String[] CITY_LIST = new String[] {
      "ANGOLA",
      "BAINS",
      "ST FRANCISVILLE",
      "TUNICA",
      "WAKEFIELD"
  };

}
