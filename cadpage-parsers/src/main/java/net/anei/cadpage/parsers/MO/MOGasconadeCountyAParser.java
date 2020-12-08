package net.anei.cadpage.parsers.MO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchGlobalDispatchParser;



public class MOGasconadeCountyAParser extends DispatchGlobalDispatchParser {
  
  private static final Pattern UNIT_PTN = Pattern.compile("(?!911)(?:[GOMB]|BE|BL|MT|NH|SJ)?\\d{2,4}|[A-Z][FP]D|BASE\\d|CHAMOIS|GAAD|HAAD|GFDTANK|MSHP|<[A-Z0-9]+>");
  
  public MOGasconadeCountyAParser() {
    super(CITY_TABLE, "GASCONADE COUNTY", "MO", LEAD_SRC_UNIT_ADDR | PLACE_FOLLOWS_CALL, null, UNIT_PTN);
    setupCallList(CALL_LIST);
    setupSaintNames("LOUIS");
    setupMultiWordStreets(
        "AHR STROM",
        "BALD HILL",
        "BEEMONT SCHOOL",
        "BEM CHURCH",
        "BLACK FOREST",
        "BLUE HOUSE",
        "BUCHOLZ HOLLOW",
        "CEDAR LANE",
        "CHAMPION CITY",
        "CHARLOTTE CHURCH",
        "CITY CEMETERY",
        "COUNTY LINE",
        "DRAKE SCHOOL",
        "ELK HEAD",
        "ERV HANS",
        "FILLA FOREST",
        "FRENE CREEK",
        "FRENE CT",
        "GARDEN CLUB",
        "GLASER HOLLOW",
        "HALL PARK",
        "HELMIG FERRY",
        "HILLSIDE TRAILER",
        "HOMETOWN PLAZA",
        "INDIAN BEND",
        "JENARIK COURT",
        "LAKE NORTHWOODS",
        "LAKE SHORE",
        "LAZY D",
        "LONE GROVE SCHOOL",
        "MARIES COUNTY",
        "MT STERLING",
        "MUD CREEK",
        "NURSING HOME",
        "OAK HAVEN",
        "OCEAN WAVE",
        "PEACEFUL VALLEY",
        "PEAR TREE TRAILER",
        "PIGG HOLLOW",
        "PUMP STATION",
        "RED BIRD",
        "RED OAK",
        "ROLLING HOME",
        "STOCK PILE",
        "STONE HILL",
        "STONY HILL",
        "SULPHER SPRINGS",
        "TOWER RIDGE",
        "TRIPLE E",
        "VIRGIL NICKS"
    );
  }
  
  @Override
  public String getFilter() {
    return "central@fidmail.com,gc911text@gmail.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("MESSAGE / ")) body = body.substring(10).trim();
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equals("FRNKLN CNTY")) data.strCity = "FRANKLIN COUNTY";
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "911 ABUSE",
      "911 ACCIDENTAL",
      "911 HANG UP",
      "911 OPEN LINE",
      "911 TRANSFER TO ANOTHER AGENCY",
      "ABANDONED VEHICLE",
      "ABDOMINAL PAIN",
      "ABDUCTION",
      "ABUSE/ABANDONMENT",
      "ACCIDENTAL INJURY",
      "ACTIVE ASSAILANT",
      "ALARM SOUNDING- EMS",
      "ALARM SOUNDING- LAW",
      "ALARM-COMMERCIAL FIRE ALARM",
      "ALARM- RESIDENTIAL FIRE ALARM",
      "ALLERGIC REACTION",
      "AMPUTATION",
      "ANIMAL BITE EMS NEEDED",
      "ANIMAL BITE NO EMS",
      "ANIMAL CALL",
      "ARREST",
      "ASSAULT VICTIM- EMS NEEDED",
      "ASSAULT",
      "ASSAULT- SEXUAL/RAPE",
      "ASSIST AN INVALID",
      "ASSIST ANOTHER AGENCY",
      "ASSIST ANOTHER AGENCY- EMS",
      "ASSIST ANOTHER AGENCY - FIRE",
      "ASSIST EMS- FIRE NEEDED",
      "ATTEMPT TO LOCATE/CONTACT",
      "ATV COMPLAINT",
      "BACK PAIN",
      "BACK UP",
      "BFD FALL",
      "BOATING ACCIDENT",
      "BODY FOUND",
      "BOMB THREAT",
      "BOMB/EXPLOSIVE FOUND",
      "BOND",
      "BROADCAST MESSAGE",
      "BRUSH - 3 OR LESS ACRES/NO EXPOSURES",
      "BRUSH - 4+ ACRES AND/OR EXPOSURES",
      "BURGLARY IN PROGRESS",
      "BURGLARY NOT IN PROGRESS",
      "BURNS",
      "C & I DRIVING",
      "CARBON MONOXIDE ILLNESS/UNKN ILLNESS",
      "CARBON MONOXIDE NO ILLNESS",
      "CARDIAC ARREST",
      "CHECK THE AREA",
      "CHECK THE WELFARE",
      "CHEST PAIN",
      "CHOKING NO FIRE NEEDED",
      "CHOKING",
      "CIVIL MATTER",
      "COLD EXPOSURE",
      "CONSERVATION CALL",
      "CONTROLLED BURN",
      "CUSTODY CALL",
      "DEATH",
      "DFS CALL",
      "DIABETIC PROBLEMS",
      "DIFFICULTY BREATHING",
      "DOMESTIC IN  PROGRESS",
      "DOMESTIC IN PROGRESS- PHYSICAL",
      "DOMESTIC NOT IN PROGRESS",
      "DROWNING- *DOMESTIC",
      "DROWNING- SPECIALIZED",
      "DRUGS/NARCOTICS",
      "DUMPSTER FIRE",
      "DUPLICATE CALL",
      "ELECTRICAL- WATER ENDANGERING",
      "ELECTROCUTION",
      "ESCORT",
      "EXPLOSION",
      "EXTRA PATROL",
      "EYE INJURY/PROBLEMS",
      "FAIL TO YIELD TO EMERGENCY VEHICLE",
      "FALL",
      "FALL- LONG FALL",
      "FALL- PERSON FELL",
      "FIGHT IN PROGRESS",
      "FLOOD",
      "FLUE FIRE CONTAINED",
      "FOLLOW UP",
      "FRAUD/DECEPTION",
      "FUNERAL",
      "GAS LEAK INSIDE COMMERCIAL STRUCTURE",
      "GAS LEAK INSIDE RESIDENCE",
      "GAS LEAK OUTSIDE",
      "GROAD CLOSURE",
      "GUNSHOT VICTIM",
      "HANGING",
      "HARASSMENT/STALKING",
      "HAZMAT",
      "HEADACHE",
      "HEART PROBLEMS",
      "HEAT EXPOSURE",
      "HEMORRHAGE/ BLEEDING",
      "HEMORRHAGE/LACARATION",
      "HEMORRHAGE/LACERATION",
      "HIT CONFIRMATION/REQUEST",
      "INDECENCY/LEWDNESS",
      "INFORMATION",
      "INSPECTION",
      "INTOXICATED DRIVER",
      "INTOXICATED PERSON",
      "INVESTIGATION\\s*-\\s*LAW",
      "JUVENILE",
      "LACERATION",
      "LANDING ZONE",
      "LEAVE WITHOUT PAY",
      "LITTERING/DUMPING",
      "LOST/FOUND PROPERTY",
      "MAILBOX VANDALISM",
      "MAJOR RESCUE",
      "MOTOR VEHICLE ACCIDENT-INJURIES",
      "MOTOR VEHICLE ACCIDENT-NO INJURIES",
      "MOTOR VEHICLE ACCIDENT-UNKNOWN INJ",
      "MOTOR VEHICLE ACCIDENT-SINKING VEHICLE",
      "MOTOR VEHICLE ACCIDENT-WITH FIRE",
      "MOTOR VEHICLE ACCIDENT WITH RESCUE",
      "MCI LEVEL 1",
      "MCI LEVEL 2",
      "MCI LEVEL 3",
      "MINOR RESCUE",
      "MISSING/RUNAWAY/FOUND PERSON",
      "MOVE UP",
      "MUTUAL AID-EMS",
      "MVA EJECTION",
      "MVA INJURIES",
      "MVA NO INJURIES",
      "MVA SINKING VEHICLE",
      "MVA UNKNOWN INJ",
      "MVA WITH FIRE",
      "MVA WITH HAZMAT",
      "MVA WITH RESCUE",
      "OBS/ NON VIOLENT",
      "ODOR OF ELECTRICAL- COMMERCIAL",
      "ODOR OF ELECTRICAL- RESIDENTIAL",
      "OPEN DOOR",
      "ORDER OF PROTECTION VIOLATION",
      "ORDINANCE VIOLATION",
      "OTHER- UNKNOWN",
      "OVERDOSE",
      "PEACE DISTURBANCE",
      "PEDESTRIAN STRUCK",
      "PHONE CALL",
      "PIPELINE EXPLOSION",
      "PLANE CRASH",
      "POISONING",
      "PR EVENT",
      "PREGNANCY COMPLICATION",
      "PREGNANCY IMMINENT DELIVERY",
      "PRISONER TRANSPORT",
      "PROPERTY DAMAGE",
      "PSYCHIATRIC PROBLEMS",
      "PSYCHIATRIC PROBLEMS- EMS",
      "PSYCHIATRIC PROBLEMS- LAW",
      "PURSUIT",
      "REKINDLE",
      "REPOSSESSION",
      "ROAD CLOSURE",
      "ROBBERY",
      "SEARCH AND RESCUE",
      "SEARCH WARRANT",
      "SEIZURES",
      "SEIZURES- BREATHING NOT VERIFIED",
      "SHOCK TIME",
      "SHOPLIFTING",
      "SHOTS FIRED",
      "SICK CASE",
      "SICK PERSON- SICK CASE",
      "SIREN TEST",
      "SLIDE OFF",
      "STABBING",
      "STAND BY",
      "STATUS CHECK OF OFFICERS",
      "STOLEN VEHICLE",
      "STRANDED MOTORIST",
      "STROKE",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE- COMMERCIAL",
      "STRUCTURE FIRE- COMMERCIAL",
      "STRUCTURE FIRE- NON DWELLING",
      "STRUCTURE FIRE- RESIDENTIAL",
      "SUICIDAL PERSON-SCENE NOT SECURE",
      "SUICIDAL PERSON-SCENE SECURE",
      "SUSPICIOUS ACTIVITY",
      "SUSPICIOUS PERSON",
      "SUSPICIOUS VEHICLE",
      "TEST CALL",
      "THEFT/LARCENY",
      "THREAT",
      "TORNADO",
      "TRAFFIC CONTROL - FIRE",
      "TRAFFIC CONTROL - LAW",
      "TRAFFIC HAZARD - LAW",
      "TRAFFIC STOP",
      "TRAFFIC VIOLATION/HAZARD",
      "TRAIN ACCIDENT",
      "TRAIN FIRE",
      "TRANSFER- ROUTINE",
      "TRANSFORMER",
      "TRAUMATIC INJURY",
      "TRESPASSING",
      "UNCONSCIOUS DIABETIC",
      "UNCONSCIOUS PERSON- NOW CONSCIOUS",
      "UNCONSCIOUS/FAINTING",
      "UNKNOWN EMS",
      "UNRESPONSIVE PERSON",
      "UNUSUAL ODOR",
      "VANDALISM IN PROGRESS",
      "VANDALISM NOT IN PROGRESS",
      "VEHICLE FIRE WITH EXPOSURES",
      "VEHICLE FIRE",
      "VIN VERIFICATION",
      "VISITOR",
      "WALK IN TO AMB BUILDING",
      "WARRANT ARREST",
      "WASHDOWN",
      "WATER RESCUE",
      "WEAPONS INCIDENT",
      "WEATHER",
      "WIRES"
  );

  private static final String[] CITY_TABLE = new String[]{
    "BLAND",
    "BELLE",
    "GASCONADE",
    "GERALD",
    "HERMANN",
    "MORRISON",
    "MT STERLING",
    "OWENSVILLE",
    "ROSEBUD",
    "SULLIVAN",
    
    "FRNKLN CNTY",
    "FRANKLIN COUNTY",
    "GASCONADE COUNTY",
    "MARIES COUNTY",
    "OUT OF COUNTY",
    "OSAGE COUNTY"
  };
}
