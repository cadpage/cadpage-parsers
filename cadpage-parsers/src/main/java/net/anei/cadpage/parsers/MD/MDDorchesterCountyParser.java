package net.anei.cadpage.parsers.MD;

import java.util.Properties;

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
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("DOR911:")) {
      body = body.substring(7).trim();
      if (!body.startsWith("CT:")) body = "CT:" + body;
    }
    if (body.endsWith(":DC")) body = body.substring(0,body.length()-3).trim();
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
      "APPLIANCE ON FIRE",
      "ASSAULT/SEXUAL ASSLT",
      "BACK PAIN",
      "BACK PAIN-NON TRAUMA",
      "BACK PAIN-NONTRAUMA",
      "BOAT FIRE W/EXPOSURE",
      "BOAT UNK DISTRESS",
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
      "CONVULSIONS/SEIZURES",
      "DIABETIC",
      "DIABETIC PROBLEMS",
      "ELECTRICAL ARCING",
      "ELECTRICAL ODOR",
      "ELEVATOR MALF OCCUPI",
      "EYE PROBLEM/INJURY",
      "FAINTING",
      "FALL",
      "FALL/PUBLIC ASSIST",
      "FALLS",
      "FIRE / EXTINGUISHED",
      "GAS LEAK OUTSIDE",
      "HEADACHE",
      "HEART PROBLEMS",
      "HEAT/COLD EXPOSURE",
      "HEMORRHAGE",
      "HEMORRHAGE/LACERATIO",
      "IMPENDING SEIZURE",
      "INTERFACILITY/TRANSF",
      "INVESTIGATION/NO ENT",
      "LANDING ZONE",
      "LIGHT SMOKE INVESTIG",
      "MEDICAL ALARM",
      "MEDICAL ASSIST",
      "MOTOR VEH. COLLISION",
      "MOTOR VEH. COL. W/EN",
      "OUTSIDE FIRE (SMALL)",
      "OVERDOSE",
      "OVERDOSE/POISONING",
      "PAGER TEST",
      "PERIFERAL ENTRAPMENT",
      "POLE FIRE",
      "PREG/CHILDBIRTH/MATR",
      "PREGNANCY/MATERNITY",
      "PSYCHIATRIC/ABNORMAL",
      "PUBLIC ASSIST",
      "RES STRUCTURE FIRE",
      "RESIDENTIAL FIRE ALA",
      "SEIZURE W/STROKE HIS",
      "SEIZURES",
      "SICK PERSON",
      "SINKING VEHICLE",
      "(SMALL) FUEL SPILL",
      "SML BRUSH/GRASS FIRE",
      "SMOKE IN AREA",
      "SPRINKLER ALARM ACTI",
      "STAB/GUNSHOT/PENETRA",
      "STANDBY",
      "STROKE (CVA)",
      "STRUCTURE FIRE",
      "TRANSFER",
      "TRASH FIRE",
      "TRAUMATIC INJURY",
      "UNCONSCIOUS/FAINTING",
      "UNCONSCIOUS SUBJECT",
      "UNKNOWN PROBLEM",
      "UNKNOWN TYPE FIRE",
      "UNKNOWN WATER RESCUE",
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