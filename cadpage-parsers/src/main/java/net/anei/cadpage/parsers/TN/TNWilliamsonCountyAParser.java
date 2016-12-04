package net.anei.cadpage.parsers.TN;


import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class TNWilliamsonCountyAParser extends DispatchB2Parser {
  
  private static final Pattern MARKER = Pattern.compile("911-CENTER:[A-Z0-9]+ +>");
  
  public TNWilliamsonCountyAParser() {
    super("911-CENTER:", TNWilliamsonCountyParser.CITY_LIST, "WILLIAMSON COUNTY", "TN");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_NAMES);
  }
  
  @Override
  public String getFilter() {
    return "911-CENTER@williamson-tn.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!MARKER.matcher(body).lookingAt()) return false;
    if (!super.parseMsg(body, data)) return false;
    data.strCity = data.strCity.toUpperCase();
    if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    return true;
  }
  
  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "ANTIOCH",       "NOLENSVILLE",
  });
  
  private static final String[] MWORD_STREET_NAMES = new String[]{
    "ARNO-COLLEGE GROVE",
    "BEECHS TAVERN",
    "BELL POND",
    "BENDING CHESTNUT",
    "BERRY'S CHAPEL",
    "BLUE HERON",
    "BOWIE HOLLOW",
    "BOWIE LAKE",
    "BRITTAIN DOWNS",
    "BRUSH CREEK",
    "BUNKER HILL",
    "BURKE HOLLOW",
    "BURKE HOLLOW VALLEY FARMS",
    "BURKITT PLACE",
    "CANEY FORK",
    "CARTERS CREEK",
    "CHEROKEE HILLS",
    "CITY CENTER",
    "CLEAR CREST",
    "CROW CUT",
    "DAVIS HOLLOW",
    "DEER LAKE",
    "DEL RIO",
    "DELTA SPRINGS",
    "DONALD WILSON",
    "DOUGLASS GLEN",
    "EAST MORAN",
    "EDEN ROSE",
    "FALLING WATER",
    "FALLS RIDGE",
    "FISHING CREEK",
    "FORT LEE",
    "GLEN LAKES",
    "GOSEY HILL",
    "HADLEY SCHOOL",
    "HALEY INDUSTRIAL",
    "HARPETH HILLS",
    "HAWKS LANDING",
    "HORN TAVERN",
    "HUNTING CREEK",
    "JACKSON WHITE",
    "JEFF HOLT",
    "JOHNSON INDUSTRIAL",
    "KINGS CAMP",
    "KYLES CREEK",
    "LAKE COLONIAL",
    "LAKE VALLEY",
    "LAMPKINS BRIDGE",
    "LEGACY HILLS",
    "LEGENDS CREST",
    "LEGENDS GLEN",
    "LEGENDS RIDGE",
    "LES WAGGONER",
    "LODGE HALL",
    "LOOKING GLASS",
    "LOS LOMAS",
    "MAPLE VILLAGE",
    "MAXWELL LANDING",
    "MEADOW BRIDGE",
    "MEADOW WOOD",
    "MER ROUGE",
    "MILE END",
    "MILTON FOX",
    "MOUNT HEBRON",
    "NATCHEZ TRACE",
    "NOLEN PARK",
    "NOLENSVILLE PARK",
    "NORTH CHAPEL",
    "OAK CREEK",
    "OAK TREE",
    "OSBURN HOLLOW",
    "OXFORD GLEN",
    "PARK VILLAGE",
    "PARKER HOLLOW",
    "PLEASANT HILL",
    "POLO CLUB",
    "POPE'S CHAPEL",
    "PUMPKIN RIDGE",
    "QUEEN ANNES",
    "ROCK CRESS",
    "ROCKY FORK",
    "ROCKY SPRINGS",
    "SCENIC HILLS",
    "SCHOOL HEIGHTS",
    "SEDONA WOODS",
    "SHADY GROVE",
    "SHADY OAK",
    "SHELDON PARK",
    "SILVER LEAF",
    "SLEEPY HOLLOW",
    "SOUTH BERRYS CHAPEL",
    "SPEARS GROVE",
    "SPENCER CREEK",
    "ST GEORGES",
    "STARNES MILL",
    "STILLHOUSE HOLLOW",
    "STONEY BROOK",
    "SUGAR RIDGE",
    "SUNSET PARK",
    "THOMPSON STATION",
    "THOMPSONS STATION",
    "VALLEY FORGE",
    "WADDELL HOLLOW",
    "WATKINS CREEK",
    "WILD TIMBER",
    "WILKINS BRANCH"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN/PROBLEMS",
      "AB PAIN/FAINTING OR NEAR =>50",
      "ALLERGIES/ENVENOMATIONS",
      "ALLERGY/ENVENOMATION/NOT ALERT",
      "ANIMAL AT LARGE",
      "ARREST/NOT BREATHING",
      "ASSIST AGENCY",
      "ASSIST CITIZEN",
      "BACK PAIN-NON TRAUMA",
      "BREATHING PROBLEMS",
      "BREATHING PROBLEMS-DISTRESS",
      "BREATHING PROBLEMS-NOT ALERT",
      "BREATHING PROBLEMS DIFF SPEAK",
      "BRUSH/FIRE",
      "CARBON MONOXIDE ALARM",
      "CHEST PAIN",
      "CHEST PAIN-BREATHING =>35",
      "CHEST PAIN-BREATHING NORM=>35",
      "CHEST PAIN-NON TRAU BR NORM<35",
      "CHEST PAIN-NOT ALERT",
      "CHEST PAIN CLAMMY",
      "CHIMNEY FIRE",
      "CHOKING-NOT CHOKING NOW",
      "DIABETIC PROBLEMS-ALERT",
      "DOMESTIC",
      "ELECTROCUTION/LIGHTNING",
      "FALLS-DANGEROUS BODY AREA",
      "FALLS-NON RECENT",
      "FALLS-NOT DANGEROUS BODY AREA",
      "FALLS-UNCONSCIOUS",
      "FALLS-UNKNOWN STATUS",
      "FALLS LONG FALL",
      "FIRE ALARM",
      "FIRE (UNKNOWN ORGIN)",
      "GENERAL ALERT",
      "HAZMAT",
      "HEADACHE-NOT ALERT",
      "HEART PROBLEMS/A.I.C.D",
      "HEMORRHAGE/LACERATIONS",
      "HOUSE FIRE",
      "INTERACT TEST",
      "INVESTIGATION",
      "LIFT ASSISTANCE",
      "MEDICAL ALARM",
      "MEDICAL CALL",
      "ODOR INVESTIGATION",
      "OVERDOSE/POISONING/UNCONSCIOUS",
      "OVERDOSE/UNKNOWN STATUS",
      "PIA",
      "POISONING-W/O PRIORITY SYMPTOM",
      "PROPERTY DAMAGE ACCIDENT",
      "PROPERTY DAMAGE ACCIDENT 28MM",
      "PSYCH/NON SUICIDAL/ALERT",
      "SEIZURE NOT SEIZING EFF BREATH",
      "SEIZURES-CONT OR MULT SEIZURES",
      "SEIZURES-DIABETIC",
      "SEIZURES-NOT ACTIVE",
      "SICK",
      "SICK ALTERED LEVEL CONS",
      "SICK-NOT ALERT",
      "SICK PERSON",
      "STROKE(CVA)-NOT ALERT",
      "STROKE(CVA)-NUMBNESS",
      "TRAUMATIC INJURY-UNCONSCIOUS",
      "UNAUTHORIZED BURN",
      "UNCONSCIOUS/FAINTING(NEAR)",
      "VEHICLE FIRE",
      "WANT OFFICER",
      "WATER/FLOW",
      "WELFARE CHECK",
      "WIRES DOWN"
  );
}