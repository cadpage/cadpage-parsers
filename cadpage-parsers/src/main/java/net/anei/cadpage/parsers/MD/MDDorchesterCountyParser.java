package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDDorchesterCountyParser extends FieldProgramParser {

  public MDDorchesterCountyParser(){
    super(CITY_CODE_TABLE, "DORCHESTER COUNTY", "MD",
           "CT:ADDR/S0L! BOX:BOX! DUE:UNIT!");
    addNauticalTerms();
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupMapAdjustReplacements(
        "BLTW",                     "BELTWAY",
        "BY-P",                     "BYPASS",
        "DORCHESTER SQUARE MALL",   "DORCHESTER SQUARE",
        "EAST NEW MARKET HURL",     "EAST NEW MARKET HURLOCK",
        "EAST NEW MARKET ELLW",     "EAST NEW MARKET ELWOOD",
        "EAST NEW MARKET RHOD",     "EAST NEW MARKET RHODESDALE"
    );
  }

  public String getFilter() {
    return "dor911@docogonet.com";
  }

  private static final Pattern LEAD_JUNK_PTN = Pattern.compile("(?:DOR911|Dorchester): *");
  private static final Pattern TRAIL_JUNK_PTN = Pattern.compile(" *:DC(?::DOR911)?$");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = LEAD_JUNK_PTN.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end());
    match = TRAIL_JUNK_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start());

    return super.parseMsg(body, data);
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
    "BEACH HAVEN",
    "BEAVER NECK VILLAGE",
    "BLINK HORN",
    "BUENA VISTA",
    "CABIN CREEK HURLOCK",
    "CABIN CREEK",
    "CAMBRIDGE MARKETPLAC",
    "CASSON NECK",
    "CASTLE HAVEN",
    "CEDAR GROVE",
    "CHURCH CREEK",
    "COOKS POINT",
    "DORCHESTER SQUARE",
    "EAST NEW MARKET E",
    "EAST NEW MARKET ELLW",
    "EAST NEW MARKET HURL",
    "EAST NEW MARKET RHOD",
    "EDLON PARK",
    "ELLIOTT ISLAND",
    "ELLWOOD CAMP",
    "GLORIA RICHARDSON",
    "GOLD RUSH",
    "GOLDEN HILL",
    "GRAVEL BRANCH",
    "GREEN POINT",
    "GRIFFITH NECK",
    "HARRISON FERRY",
    "HOOPERS ISLAND",
    "HORNS POINT",
    "HUDSON SCHOOL",
    "JONES VILLAGE",
    "LORDS CROSSING",
    "LUCY FISH",
    "MADISON CANNING HOUS",
    "MAIDEN FOREST",
    "MAPLE DAM",
    "MOUNT HOLLY",
    "MOUNT ZION",
    "MT HOLLY",
    "NORTH TARA",
    "OYSTER SHELL POIN",
    "OYSTER SHELL POINT",
    "PALMER MILL",
    "PALMERS MILL",
    "PINE TOP",
    "RHODESDALE VIENNA",
    "ROBBINS FARM",
    "ROLLING ACRES",
    "RYANS RUN",
    "SANDY ACRES",
    "SANDY HILL",
    "SCHOOL HOUSE",
    "SHILOH CAMP",
    "SHILOH CHURCH HURLOC",
    "SKEET CLUB",
    "STEELE NECK",
    "STEAMER RUN",
    "STONE BOUNDARY",
    "SUICIDE BRIDGE",
    "TATES BANK",
    "TAYLORS ISLAND",
    "WADDELLS CORNER",
    "WEST CENTRAL",
    "WEST VIEW",
    "WILLIAMSBURG CHURCH",
    "WOODLAND ACRES"

  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "911 TEST CALL",
      "ABDOMINAL PAINS",
      "ABSORBANT NEEDED",
      "ALARM - APARTMENT",
      "ALARM - COMMERCIAL",
      "ALARM - RESIDENCE",
      "ALLERGIC REACTION",
      "ALLERGIC/REACTION",
      "ANIMAL BITE",
      "APPLIANCE ON FIRE",
      "ASSAULT/SEXUAL ASSLT",
      "BACK PAIN",
      "BACK PAIN-NON TRAUMA",
      "BACK PAIN-NONTRAUMA",
      "BOAT CHECK WELFARE",
      "BOAT FIRE / COASTAL",
      "BOAT FIRE W/EXPOSURE",
      "BOAT UNK DISTRESS",
      "BOX ALARM",
      "BREATHING PROBLEMS",
      "BRUSH FIRE (BIG)",
      "BRUSH FIRE (LARGE)",
      "BRUSH FIRE (SMALL)",
      "BURNS/EXPLOSION",
      "CARDIAC/RESP ARREST",
      "CHEST PAIN",
      "CHEST PAINS",
      "CHILDBIRTH",
      "CHOKING",
      "CO/INHALATION/HAZMAT",
      "COMMERCIAL BUILD. FI",
      "COMMERCIAL BUILDING",
      "COMMERCIAL FIRE ALAR",
      "COMMERCIAL LOCAL BOX",
      "CONVULSIONS/SEIZURES",
      "DIABETIC",
      "DIABETIC PROBLEMS",
      "ELECTRICAL ARCING",
      "ELECTRICAL ODOR",
      "ELEVATOR MALF OCCUPI",
      "EMS TRANSFER",
      "EYE PROBLEM/INJURY",
      "FAINTING",
      "FALL",
      "FALL/PUBLIC ASSIST",
      "FALLS",
      "FIRE / EXTINGUISHED",
      "GARAGE FIRE",
      "GAS LEAK OUTSIDE",
      "HEADACHE",
      "HEART PROBLEMS",
      "HEAT/COLD EXPOSURE",
      "HEMORRHAGE",
      "HEMORRHAGE/LACERATIO",
      "HOTEL FIRE",
      "IMPENDING SEIZURE",
      "INTERFACILITY/TRANSF",
      "INVESTIGATION/NO ENT",
      "LANDING ZONE",
      "LGTNG STRKE RESIDENC",
      "LIGHT SMOKE INVESTIG",
      "LOCAL BOX",
      "MARINE BOX",
      "(MASS CAS. INC.)",
      "MEDICAL ALARM",
      "MEDICAL ASSIST",
      "MEDICAL-ALPHA",
      "MEDICAL-BRAVO",
      "MEDICAL-CHARLIE",
      "MEDICAL-DELTA",
      "MEDICAL-ECHO",
      "MEDICAL-OMEGA",
      "MOTOR VEH. COLLISION",
      "MOTOR VEH. COL. W/EN",
      "MOTOR VEH. W/ROLL",
      "MULTI-RESIDENT FIRE",
      "MVC UNKN SITUATION",
      "MVC W/ INJURIES",
      "ODOR OF GAS INSIDE",
      "OUTBUILDING FIRE",
      "OUTSIDE FIRE (SMALL)",
      "OVERDOSE",
      "OVERDOSE/POISONING",
      "PAGER TEST",
      "PEOPLE IN WATER",
      "PERIFERAL ENTRAPMENT",
      "PLANE FIRE ON GROUND",
      "POLE FIRE",
      "PREG/CHILDBIRTH/MATR",
      "PREGNANCY/MATERNITY",
      "PSYCHIATRIC/ABNORMAL",
      "PUBLIC ASSIST",
      "RES STRUCTURE FIRE",
      "RESCUE BOX",
      "RESCUE LOCAL",
      "RESIDENTIAL CO DETEC",
      "RESIDENTIAL FIRE ALA",
      "RESIDENTIAL RESCUE",
      "SEIZURE W/STROKE HIS",
      "SEIZURES",
      "SHED FIRE",
      "SICK PERSON",
      "SINKING VEHICLE",
      "(SMALL) FUEL SPILL",
      "STABBING",
      "SML BRUSH/GRASS FIRE",
      "SMOKE IN AREA",
      "SPRINKLER ALARM ACTI",
      "STAB/GUNSHOT/PENETRA",
      "STANDBY",
      "STROKE (CVA)",
      "STRUCTURE FIRE",
      "SUBJECT IN THE WATER",
      "TRANSFER",
      "TRASH FIRE",
      "TRAUMATIC INJURY",
      "UNCONSCIOUS/FAINTING",
      "UNCONSCIOUS SUBJECT",
      "UNKNOWN PROBLEM",
      "UNKNOWN TYPE FIRE",
      "UNKNOWN WATER RESCUE",
      "WATER RESCUE BOX",
      "WOODS FIRE",
      "VEHICLE FIRE",
      "VEHICLE FIRE W/EXPOS",
      "WORKING FIRE TSK FRC",
      "XPLOSION-RESIDENTIAL"
  );

  private static final Properties CITY_CODE_TABLE =
    buildCodeTable(new String[]{
        "BISH", "BISHOPS HEAD",
        "CAMB", "CAMBRIDGE",
        "CHUR", "CHURCH CREEK",
        "EAST", "EAST NEW MARKET",
        "FEDE", "FEDERALSBURG",
        "HURL", "HURLOCK",
        "LINK", "LINKWOOD",
        "MADI", "MADISON",
        "PRES", "PRESTON",
        "RHOD", "RHODESDALE",
        "SEAF", "RHODESDALE",    // ????
        "SECR", "SECRETARY",
        "TAYL", "TAYLORS ISLAND",
        "TODD", "TODDVILLE",
        "VIEN", "VIENNA",
        "WOOL", "WOOLFORD"
    });
}