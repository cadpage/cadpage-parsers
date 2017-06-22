package net.anei.cadpage.parsers.MS;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB3Parser;

public class MSDeSotoCountyAParser extends DispatchB3Parser {
  
  private static final Pattern DIR_BOUND_PTN = Pattern.compile("\\b([NSEW])/B\\b");

  public MSDeSotoCountyAParser() {
    super("911CENTER:", CITY_LIST, "DESOTO COUNTY", "MS", B2_FORCE_CALL_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
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
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "BUSINESS CENTER",
    "CAMP CREEK",
    "CARTER LANE",
    "CENTER HILL",
    "CREEK SIDE",
    "DEVON PARK",
    "FOX HUNTERS",
    "GEE GEE",
    "HACKS CROSS",
    "HUNTERS HILL",
    "HWY 178",
    "HWY 302",
    "JOE LYON",
    "LAUREL HILL",
    "METHODIST HOSPITAL",
    "PIGEON ROOST",
    "PLANTATION RIDGE",
    "PLEASANT HILL",
    "POLK LANE",
    "REGAL BEND",
    "SANDIDGE CENTER",
    "SHADY GROVE",
    "ST ANDREWS",
    "ST MICHAEL THOMAS",
    "STONEY BROOK",
    "TIMBER OAKS",
    "WHISPERING PINES"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "10 CHEST PAIN / DISCOMFORT",
      "12 CONVULSIONS / SEIZURES",
      "13 DIABETIC PROBLEMS",
      "17 FALLS WITH INJURY",
      "19 HEART PROBLEMS / A.I.C.D.",
      "20 HEAT / COLD EXPOSURE",
      "21 HEMORRHAGE / LACERATIONS",
      "26 SICK PERSON (SPECIFIC DIAG)",
      "28 STROKE (CVA) - TIA",
      "29 MVA WITH INJURIES",
      "30 TRAUMATIC INJURIES",
      "31 UNCONSCIOUS / FAINTING",
      "4 ASSAULT / RAPE WITH INJURY",
      "5 BACK PAIN (NON TRAUMATIC)",
      "6 BREATHING PROBLEMS",
      "ELECTRICAL SHORT / NO SMOKE",
      "FIRE ALARM - COMMERCIAL",
      "FIRE ALARM - RESIDENTIAL",
      "GRASS / BRUSH / TREE FIRE",
      "LIFTING ASSISTANCE (NO INJURY)",
      "MEDICAL ALARM",
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
    "WEST DAYS"
  };
}
