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
      "BANNER SQUARE",
      "BARREL SPRINGS HOLLOW",
      "BEAR CREEK",
      "BEECHS TAVERN",
      "BELL POND",
      "BELLE BROOK",
      "BENDING CHESTNUT",
      "BENT CREEK",
      "BERRY'S CHAPEL",
      "BEULAH CHURCH",
      "BIG OAK",
      "BIG SKY",
      "BIRCH BARK",
      "BLACKBERRY ESTATES",
      "BLUE HERON",
      "BOIS D'ARC",
      "BOWIE HOLLOW",
      "BOWIE LAKE",
      "BRITTAIN DOWNS",
      "BRUSH CREEK",
      "BUNKER HILL",
      "BURKE HOLLOW VALLEY FARMS",
      "BURKE HOLLOW",
      "BURKITT PLACE",
      "CANEY FORK",
      "CARTERS CREEK",
      "CHASE POINT",
      "CHEROKEE HILLS",
      "CITY CENTER",
      "CLEAR CREST",
      "COUNTRY CLUB",
      "COVERED BRIDGE",
      "CROW CUT",
      "DAVIS HOLLOW",
      "DEER LAKE",
      "DEL RIO",
      "DELTA SPRINGS",
      "DONALD WILSON",
      "DOUGLASS GLEN",
      "DR ROBINSON",
      "DRAG STRIP",
      "DURHAM MANOR",
      "EAST MORAN",
      "EDEN ROSE",
      "FALLING WATER",
      "FALLS RIDGE",
      "FISHING CREEK",
      "FORT LEE",
      "FRENCH RIVER",
      "GENERAL N B FORREST",
      "GLEN LAKES",
      "GOSEY HILL",
      "GREY CLIFF",
      "GUY FERRELL",
      "HADLEY SCHOOL",
      "HALEY INDUSTRIAL",
      "HARPETH HILLS",
      "HAWKS LANDING",
      "HICKORY HILLS",
      "HIDDEN LAKE",
      "HIGH MEADOW",
      "HIGH POINT RIDGE",
      "HILL HUGHES",
      "HOLLY TREE GAP",
      "HORN TAVERN",
      "HUNTING CREEK",
      "INDIAN HEAD",
      "JACKSON WHITE",
      "JEFF HOLT",
      "JERSEY FARM",
      "JOHNSON INDUSTRIAL",
      "JONAHS RIDGE",
      "KINGS CAMP",
      "KINNARD SPRINGS",
      "KYLES CREEK",
      "LAKE COLONIAL",
      "LAKE VALLEY",
      "LAMPKINS BRIDGE",
      "LEGACY HILLS",
      "LEGENDS CREST",
      "LEGENDS GLEN",
      "LEGENDS RIDGE",
      "LEIPERS CREEK",
      "LEONARD CREEK",
      "LES WAGGONER",
      "LODGE HALL",
      "LONE STAR",
      "LOOKING GLASS",
      "LOS LOMAS",
      "MAPLE VILLAGE",
      "MAXWELL LANDING",
      "MEADOW BRIDGE",
      "MEADOW BROOK",
      "MEADOW WOOD",
      "MER ROUGE",
      "MILE END",
      "MILTON FOX",
      "MOBLEY'S CUT",
      "MOUNT HEBRON",
      "NATCHEZ TRACE",
      "NOLEN PARK",
      "NOLENSVILLE PARK",
      "NORTH BERRYS CHAPEL",
      "NORTH CHAPEL",
      "NORWEGIAN RED",
      "OAK CREEK",
      "OAK TREE",
      "OSBURN HOLLOW",
      "OWEN HILL",
      "OXFORD GLEN",
      "PARK VILLAGE",
      "PARKER HOLLOW",
      "PLEASANT HILL",
      "POLO CLUB",
      "POOL FORGE BRIDGE",
      "POPE'S CHAPEL",
      "POPES CHAPEL",
      "PUMPKIN RIDGE",
      "QUEEN ANNES",
      "RIVER LANDING",
      "ROBIN HILL",
      "ROCK CRESS",
      "ROCK WALL",
      "ROCKY FORK",
      "ROCKY SPRINGS",
      "ROUND TREE",
      "RUNNING SPRINGS",
      "SAM DONALD",
      "SCENIC HILLS",
      "SCHOOL HEIGHTS",
      "SEDONA WOODS",
      "SHADY GROVE",
      "SHADY OAK",
      "SHAMROCK MEADOWS",
      "SHELDON PARK",
      "SILVER LEAF",
      "SLEEPY HOLLOW",
      "SOUTH BERRYS CHAPEL",
      "SPEARS GROVE",
      "SPENCER CREEK",
      "SPENCER MILL",
      "SPLIT LOG",
      "ST ANDREWS",
      "ST GEORGES",
      "STAGS LEAP",
      "STARNES MILL",
      "STATION SOUTH",
      "STILLHOUSE HOLLOW",
      "STONEBRIDGE PARK",
      "STONEY BROOK",
      "SUGAR CAMP HOLLOW",
      "SUGAR MAPLE",
      "SUGAR RIDGE",
      "SUNSET PARK",
      "TALLEY HOLLOW",
      "THOMPSON STATION",
      "THOMPSONS RIDGE",
      "THOMPSONS STATION",
      "TREVOR FALLS",
      "UNION VALLEY",
      "VALLEY FORGE",
      "WADDELL HOLLOW",
      "WARREN HOLLOW",
      "WATKINS CREEK",
      "WHISPERING HILLS",
      "WILD TIMBER",
      "WILKINS BRANCH"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN/PROBLEMS",
      "AB PAIN/FAINTING OR NEAR =>50",
      "AB PAIN/FEM. PAIN ABV NAV =>45",
      "AB PAIN/FEMALE FAINT 12-50",
      "ALLERGIES/ENVENOMATIONS",
      "ALLERGY/ENVENOMATION/NOT ALERT",
      "ANIMAL AT LARGE",
      "ARREST/NOT BREATHING",
      "ASSIST AGENCY",
      "ASSIST CITIZEN",
      "BACK PAIN-NON TRAUMA",
      "BIRTH-IMMENIENT DELIVERY",
      "BOLO",
      "BREATHING PROBLEMS",
      "BREATHING PROBLEMS-DISTRESS",
      "BREATHING PROBLEMS-NOT ALERT",
      "BREATHING PROBLEMS DIFF SPEAK",
      "BRUSH/FIRE",
      "CARBON MONOXIDE ALARM",
      "CHEST PAIN",
      "CHEST PAIN-ABNORMAL BREATHING",
      "CHEST PAIN-BREATHING =>35",
      "CHEST PAIN-BREATHING NORM=>35",
      "CHEST PAIN-NON TRAU BR NORM<35",
      "CHEST PAIN-NOT ALERT",
      "CHEST PAIN CLAMMY",
      "CHIMNEY FIRE",
      "CHOKING-NOT CHOKING NOW",
      "DIABETIC PROBLEMS-ALERT",
      "DIABETIC PROBLEMS - NOT ALERT",
      "DOMESTIC",
      "ELECTROCUTION/LIGHTNING",
      "FALLS-DANGEROUS BODY AREA",
      "FALLS-NON RECENT",
      "FALLS-NOT ALERT",
      "FALLS-NOT DANGEROUS BODY AREA",
      "FALLS-SERIOUS HEMORRHAGE",
      "FALLS-UNCONSCIOUS",
      "FALLS-UNKNOWN STATUS",
      "FALLS LONG FALL",
      "FIRE ALARM",
      "FIRE (UNKNOWN ORGIN)",
      "GENERAL ALERT",
      "HAZMAT",
      "HEADACHE-NOT ALERT",
      "HEART PROB/NOT ALERT",
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
      "SEIZURES-BREATHING NOT VERIF",
      "SEIZURES-CONT OR MULT SEIZURES",
      "SEIZURES-DIABETIC",
      "SEIZURES-NOT ACTIVE",
      "SEIZURES-NOT BREATHING",
      "SICK",
      "SICK ALTERED LEVEL CONS",
      "SICK-NOT ALERT",
      "SICK PERSON",
      "SICK PERSON NO PRIORITY SYMP",
      "SMOKE INVESTIGATION",
      "SPECIAL ASSIGNMENT",
      "STROKE(CVA)-NOT ALERT",
      "STROKE(CVA)-NUMBNESS",
      "TRAUMATIC INJURY-NON DANGER AR",
      "TRAUMATIC INJURY-UNCONSCIOUS",
      "UNAUTHORIZED BURN",
      "UNCONSCIOUS/FAINTING",
      "UNCONSCIOUS/FAINTING(NEAR)",
      "VEHICLE FIRE",
      "WANT OFFICER",
      "WATER/FLOW",
      "WELFARE CHECK",
      "WIRES DOWN"
  );
}