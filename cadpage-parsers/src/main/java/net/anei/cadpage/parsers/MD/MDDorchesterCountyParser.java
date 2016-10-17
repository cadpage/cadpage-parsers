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
    "BLINK HORN",
    "CABIN CREEK HURLOCK",
    "CABIN CREEK",
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
    "PALMER MILL",
    "PALMERS MILL",
    "PINE TOP",
    "ROBBINS FARM",
    "ROLLING ACRES",
    "RYANS RUN",
    "SANDY ACRES",
    "SANDY HILL",
    "SCHOOL HOUSE",
    "SHILOH CAMP",
    "SHILOH CHURCH HURLOC",
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
      "ALARM - COMMERCIAL",
      "ALARM - RESIDENCE",
      "ALLERGIC/REACTION",
      "ASSAULT/SEXUAL ASSLT",
      "BACK PAIN-NON TRAUMA",
      "BACK PAIN-NONTRAUMA",
      "BOAT FIRE W/EXPOSURE",
      "BOAT UNK DISTRESS",
      "BREATHING PROBLEMS",
      "BRUSH FIRE (BIG)",
      "BRUSH FIRE (LARGE)",
      "BRUSH FIRE (SMALL)",
      "CARDIAC/RESP ARREST",
      "CHEST PAIN",
      "CHEST PAINS",
      "CHOKING",
      "COMMERCIAL BUILD. FI",
      "COMMERCIAL BUILDING",
      "COMMERCIAL FIRE ALAR",
      "CONVULSIONS/SEIZURES",
      "DIABETIC",
      "DIABETIC PROBLEMS",
      "EYE PROBLEM/INJURY",
      "FALL",
      "FALL/PUBLIC ASSIST",
      "FALLS",
      "GAS LEAK OUTSIDE",
      "HEADACHE",
      "HEART PROBLEMS",
      "HEAT/COLD EXPOSURE",
      "HEMORRHAGE/LACERATIO",
      "INVESTIGATION/NO ENT",
      "MEDICAL ASSIST",
      "MOTOR VEH. COLLISION",
      "MOTOR VEH. COL. W/EN",
      "OVERDOSE/POISONING",
      "PAGER TEST",
      "PERIFERAL ENTRAPMENT",
      "POLE FIRE",
      "PREG/CHILDBIRTH/MATR",
      "RESIDENTIAL FIRE ALA",
      "SICK PERSON",
      "SINKING VEHICLE",
      "(SMALL) FUEL SPILL",
      "STAB/GUNSHOT/PENETRA",
      "STANDBY",
      "STROKE (CVA)",
      "STRUCTURE FIRE",
      "TRANSFER",
      "TRAUMATIC INJURY",
      "UNCONSCIOUS/FAINTING",
      "UNKNOWN PROBLEM",
      "VEHICLE FIRE",
      "VEHICLE FIRE W/EXPOS",
      "WORKING FIRE TSK FRC"
  );
  
  private static final Properties CITY_CODE_TABLE = 
    buildCodeTable(new String[]{
        "CAMB", "CAMBRIDGE",
        "CHUR", "CHURCH CREEK",
        "EAST", "EAST NEW MARKET",
        "FEDE", "FEDERALSBURG",
        "HURL", "HURLOCK",
        "LINK", "LINKWOOD",
        "MADI", "MADISON",
        "RHOD", "RHODESDALE",
        "SEAF", "RHODESDALE",    // ????
        "SECR", "SECRETARY",
        "TAYL", "TAYLORS ISLAND",
        "VIEN", "VIENNA"
    });
}