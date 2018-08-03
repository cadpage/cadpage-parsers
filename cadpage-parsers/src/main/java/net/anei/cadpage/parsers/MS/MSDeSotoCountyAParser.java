package net.anei.cadpage.parsers.MS;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class MSDeSotoCountyAParser extends DispatchB2Parser {
  
  private static final Pattern DIR_BOUND_PTN = Pattern.compile("\\b([NSEW])/B\\b");

  public MSDeSotoCountyAParser() {
    super("911CENTER:||E-911:", CITY_LIST, "DESOTO COUNTY", "MS", B2_FORCE_CALL_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSpecialStreets(SPECIAL_STREET_LIST);
    setupSaintNames("ANDREWS");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = DIR_BOUND_PTN.matcher(body).replaceAll("$1B");
    return super.parseMsg(subject, body, data);
  }
  
  private static final Pattern COR_PTN = Pattern.compile("\\bCOR\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapAddress(String addr) {
    addr =  COR_PTN.matcher(addr).replaceAll("CORNER");
    return addr;
  }
  
  private static final String[] SPECIAL_STREET_LIST = new String[]{
      "BRAYBOURNE MAIN",
      "GREYHAWK COVE",
      "OXBOURNE",
      "MCA",
      "MITHCELLS COR",
      "ROLLINS COVE",
      "SANDBOURNE",
      "WHITE HAWK CROSS"
  };
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "BELL RIDGE",
    "BELL WOOD",
    "BELMORE LAKES",
    "BILLY PAT",
    "BUSINESS CENTER",
    "CAMP CREEK",
    "CARA BEND",
    "CARTER LANE",
    "CEDAR CREST",
    "CENTER HILL",
    "CHAMBERLIN OAK",
    "CHAPEL RIDGE",
    "CRAFT GOODMAN FRONTAGE",
    "CREEK SIDE",
    "DEVON PARK",
    "DEW BERRY",
    "FARM POND",
    "FOX GLEN",
    "FOX HUNT",
    "FOX HUNTERS",
    "FOX RUN",
    "GEE GEE",
    "GRASS POND",
    "GRAYS CREEK",
    "GREEN VILLAGE",
    "HACKS CROSS",
    "HAWKS CROSSING",
    "HOLLY SPRINGS",
    "HONEY SUCKLE",
    "HUNTERS HILL",
    "HWY 178",
    "HWY 302",
    "JOE LYON",
    "KYLE DURAN",
    "LAUREL HILL",
    "LAZY CREEK",
    "LOW BRIDGE",
    "MARY JANE",
    "METHODIST HOSPITAL",
    "OAK FOREST",
    "OAK HEIGHTS",
    "PARKVIEW OAKS",
    "PAUL COLEMAN",
    "PIGEON ROOST",
    "PLANTATION RIDGE",
    "PLEASANT HILL",
    "POLK LANE",
    "RACHEL SHEA",
    "RED BANKS",
    "REGAL BEND",
    "ROBERTSON GIN",
    "SANDIDGE CENTER",
    "SHADY GROVE",
    "SHALLOW CREEK",
    "SPRING CREEK",
    "SPRING HILL",
    "SPRING VALLEY",
    "ST ANDREWS",
    "ST MICHAEL THOMAS",
    "STONE GARDEN",
    "STONE PARK",
    "STONEY BROOK",
    "STRAW BRIDGE",
    "TALLY HO",
    "TIMBER OAKS",
    "TRINITY PARK",
    "WEDGE HILL",
    "WHISPERING PINES",
    "WIND PARK E"

  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "1 ABDOMINAL PAIN / PROBLEMS",
      "10 CHEST PAIN / DISCOMFORT",
      "12 CONVULSIONS / SEIZURES",
      "13 DIABETIC PROBLEMS",
      "17 FALLS WITH INJURY",
      "19 HEART PROBLEMS / A.I.C.D.",
      "20 HEAT / COLD EXPOSURE",
      "21 HEMORRHAGE / LACERATIONS",
      "22 OTHER ENTRAPMENTS (NON MVA)",
      "26 SICK PERSON (SPECIFIC DIAG)",
      "28 STROKE (CVA) - TIA",
      "29 MVA WITH INJURIES",
      "2 ALLERGIC REACTIONS / STINGS",
      "30 TRAUMATIC INJURIES",
      "31 UNCONSCIOUS / FAINTING",
      "4 ASSAULT / RAPE WITH INJURY",
      "5 BACK PAIN (NON TRAUMATIC)",
      "6 BREATHING PROBLEMS",
      "9 CARDIAC-RESP ARREST / DEATH",
      "ACCIDENT UNKOWN",
      "ACCIDENT UNKOWN INJURIES",
      "ACCIDENT W/INJURIES",
      "ACCIDENT W/INJURIES HOLLY",
      "ACCIDENT W/OUT INJ",
      "ALL OTHER FIRE",
      "ATTEMPT TO LOCATE",
      "BREATHING PROBLEMS",
      "CARBON MONOXIDE DETECTOR",
      "DUMPSTER FIRE",
      "ELECTICAL SHORT / NO SMOKE",
      "ELECTICAL SHORT/MO SMOKE",
      "FALLS",
      "FIRE ALARM BUSINESS",
      "FIRE ALARM - COMMERCIAL",
      "FIRE ALARM RESIDENCE",
      "FIRE ALARM - RESIDENTIAL",
      "FIRE - OTHER",
      "GAS LEAK-SPILL",
      "GRASS-BRUSH-TREE FIRE",
      "LIFTING ASSISTANCE (NO INJURY)",
      "MEDICAL ALARM",
      "POWER LINE DOWN",
      "SICK CALL PERSON",
      "SEIZURES/CONVULSIONS",
      "STROKE/CVA",
      "STRUCTURE FIRE",
      "TRANSFORMER FIRE",
      "UNCONSCIOUS/FAINTING",
      "VEHICLE FIRE"
  );

  static final String[] CITY_LIST = new String[]{
    
    // Cities
    "HERNANDO",
    "HORN LAKE",
    "OLIVE BRANCH",
    "SOUTHAVEN",

    // Towns
    "WALLS",

    // Villages
    "MEMPHIS",

    // Census-designated places
    "BRIDGETOWN",
    "LYNCHBURG",

    // Unincorporated communities
    "COCKRUM",
    "DAYS",
    "EUDORA",
    "LAKE CORMORANT",
    "LAKE VIEW",
    "LEWISBURG",
    "LOVE",
    "MINERAL WELLS",
    "NESBIT",
    "NORFOLK",
    "PLEASANT HILL",
    "WEST DAYS",
    
    // Marshall County
    "BYHALIA",
    
    // Tate County
    "INDEPENDENCE"
  };
}
