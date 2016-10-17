package net.anei.cadpage.parsers.IN;


import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/*
 * Hamilton County, IN (variant B)
 */
public class INHamiltonCountyBParser extends DispatchOSSIParser {
 
  public INHamiltonCountyBParser() {
    super(CITY_CODES, "HAMILTON COUNTY", "IN",
           "MASH INFO+");
    setupCallList(new CodeSet(CALL_TABLE));
  }
  
  @Override
  public String getFilter() {
    return "CAD@noblesville.in.us";
  }
  
  private static final Pattern MASH_PRI_PTN = Pattern.compile("^(\\d) +");
  private static final Pattern MASH_UNIT_PTN = Pattern.compile("^([A-Z]+\\d+\\b[,A-Z0-9]*) +");
  private static final Pattern MASH_PLACE_PTN = Pattern.compile(" *\\(S\\) *(.*?) *\\(N\\) *");
  private static final Pattern MASH_APT_PTN = Pattern.compile("^(APT|ROOM|RM|SUITE|BLDG) +([^ ]+)\\b");
  private static final Pattern MASH_SRC_PTN = Pattern.compile("^\\d{2}\\b");
  private class MashField extends Field {
    @Override
    public void parse(String field, Data data) {
      
      int pt = field.indexOf("Event spawned from");
      if (pt >= 0) field = field.substring(0,pt).trim();
      
      if (field.startsWith("FYI:")) field = field.substring(4).trim();
      else if (field.startsWith("Update:")) field = field.substring(7).trim();
      
      Matcher match = MASH_PRI_PTN.matcher(field);
      if (match.find()) {
        data.strPriority = match.group(1);
        field = field.substring(match.end());
      }
      
      match = MASH_UNIT_PTN.matcher(field);
      if (match.find()) {
        data.strUnit = match.group(1);
        field = field.substring(match.end());
      }
      
      int flags = 0;
      String addr = field;
      String extra = null;
      match = MASH_PLACE_PTN.matcher(addr);
      if (match.find()) {
        flags = FLAG_ANCHOR_END;
        data.strPlace = match.group(1);
        extra = addr.substring(match.end()).trim();
        addr = addr.substring(0,match.start()).trim();
      }
      
      parseAddress(StartType.START_CALL_PLACE, FLAG_START_FLD_REQ | flags, addr, data);
      data.strCall = convertCodes(data.strCall, CALL_TABLE);
      if (extra == null) extra = getLeft();
      
      match = MASH_APT_PTN.matcher(extra);
      if (match.find()) {
        String key = match.group(1);
        String apt = match.group(2);
        char chr = key.charAt(0);
        if (chr != 'R' && chr != 'A') apt = key + ' ' + apt;
        data.strApt = apt;
        extra = extra.substring(match.end()).trim();
      }
      
      match = MASH_SRC_PTN.matcher(extra);
      if (match.find()) {
        data.strSource = match.group();
        extra = extra.substring(match.end()).trim();
      }
      data.strSupp = extra;
    }
    
    @Override
    public String getFieldNames() {
      return "PRI UNIT CALL PLACE ADDR APT CITY X SRC";
    }
    
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Radio Channel:")) {
        data.strChannel = field.substring(14).trim();
        return;
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CH INFO";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MASH")) return new MashField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ADVN", "ADVANCE",
      "ALEX", "ALEXANDRIA",
      "ANDR", "ANDERSON",
      "ARC",  "ARCADIA",
      "ATL",  "ATLANTA",
      "AVON", "AVON",
      "BBRG", "BROWNSBURG",
      "BGRV", "BEECH GROVE",
      "CAR",  "CARMEL",
      "CHAR", "CHAROTTESVILLE",
      "CIC",  "CICERO",
      "CLAY", "CLAYTON",
      "CLFX", "COLFAX",
      "CLRK", "CLARKS HILL",
      "CMBY", "CAMBY",
      "COAT", "COATSVILLE",
      "CRTH", "CARTHAGE",
      "DANV", "DANVILLE",
      "ELWD", "ELWOOD",
      "FIS",  "FISHERS",
      "FMNT", "FAIRMOUNT",
      "FNTN", "FOUNTAINTOWN",
      "FORE", "FOREST",
      "FORT", "FORTVILLE",
      "FRFT", "FRANKFORT",
      "FRTN", "FRANKTON",
      "GRNF", "GREENFIELD",
      "HILL", "HILLSBURG",
      "IND",  "INDIANAPOLIS",
      "INGL", "INGALLS",
      "JAME", "JAMESTOWN",
      "KEMP", "KEMPTON",
      "KIRK", "KIRKLIN",
      "LAPL", "LAPEL",
      "LEBN", "LEBANON",
      "LZTN", "LIZTON",
      "MCCD", "MCCORDSVILLE",
      "MICH", "MICHIGANTOWN",
      "MIDD", "MIDDLETOWN",
      "MILT", "MILTON",
      "MORR", "MORRISTOWN",
      "MRKL", "MARKLEVILLE",
      "MULB", "MULBERRY",
      "NEWP", "NEW PALESTINE",
      "NEWR", "NEW ROSS",
      "NOB",  "NOBLESVILLE",
      "NSLM", "NORTH SALEM",
      "PEND", "PENDLETON",
      "PITT", "PITTSBORO",
      "PLNF", "PLAINFIELD",
      "ROSS", "ROSSVILLE",
      "RUSS", "RUSSIAVILLE",
      "SHE",  "SHERIDAN",
      "SHRL", "SHIRLEY",
      "SHRP", "SHARPSVILLE",
      "STIL", "STILESVILLE",
      "SUMM", "SUMMITVILLE",
      "THRN", "THORNTOWN",
      "TIPT", "TIPTON",
      "WES",  "WESTFIELD",
      "WHTN", "WHITESTOWN",
      "WILK", "WILKINSON",
      "WIND", "WINDFALL",
      "ZION", "ZIONSVILLE"

  });
  
  
  private static final Properties CALL_TABLE = buildCodeTable(new String[]{
      "10-50 PI",   "PI ACCIDENT - REGULAR RESP",
      "10-50 PI 1",   "PI ACCIDENT - REGULAR RESP 1",
      "10-50 PI 2",   "PI ACCIDENT - REGULAR RESP 2",
      "10-89",      "WMD - BOMB THREAT",
      "ABD PAIN",   "ABDOMINAL PAINS / PROBLEMS",
      "AIR CRSH",   "AIRCRAFT - CRASH",
      "AIR EMRG",   "AIRCRAFT - EMERGENCY / STANDBY",
      "AIR FIRE",   "AIRCRAFT - FIRE",
      "AL REACT",   "ALLERGIC REACTION / STINGS",
      "ANML RES",   "SERVICE CALL - ANIMAL RESCUE",
      "APPL FIR",   "ELECTRICAL - APPLIANCE FIRE",
      "APT ALRM",   "ALARM - APT / CONDO / MOTEL",
      "APT FIRE",   "STRUCTURE FIRE - APT / CONDO",
      "ARSN INV",   "MUTUAL AID - INVEST TASK FORCE",
      "ASST OTH",   "SERVICE CALL - ASSIST OTH DEPT",
      "BARN FIR",   "STRUCTURE FIRE - BARN/STORAGE",
      "BCK PAIN",   "BACK PAIN",
      "BLDG COL",   "RESCUE - STRUCTURE COLLAPSE",
      "BOAT DET",   "MARINE - WATERCRFT IN DISTRESS",
      "BOAT FIR",   "MARINE - BOAT / DOCK FIRE",
      "BOMB DEV",   "WMD - ORDNANCE / EXPLOSIVES",
      "BURNS",      "BURNS / SCALDS",
      "BUSN ALM",   "ALARM - COMMERICAL / BUSINESS",
      "BUSN FIR",   "STRUCTURE FIRE - COMMERICIAL",
      "CARD ARR",   "CARDIAC OR RESP ARREST / DEATH",
      "CH BIRTH",   "PREGNANCY / BIRTH / MISCARRAGE",
      "CHOKING",    "CHOKING",
      "CHST PNS",   "CHEST PAINS",
      "CIT ASST",   "SERVICE CALL - ASSIST PUBLIC",
      "CO ALRM",    "ALARM - CARBON MONOXIDE",
      "CO INHAL",   "CO / HAZMAT /CBRN / INHALATION",
      "COMM VEH",   "VEHICLE - COMMERICIAL STYLE",
      "CONF SP",    "RESCUE - CONFINED SPACE/TRENCH",
      "DIABETIC",   "DIABETIC PROBLEMS",
      "DIFF BRE",   "BREATHING PROBLEMS",
      "DROWNING",   "DROWNING / DIVING / SCUBA ACCD",
      "ELEC INV",   "ELECTRICAL - INVESTIGATION",
      "ELECTROC",   "ELECTROCUTION / LIGHTNING STRK",
      "ELEV RES",   "RESCUE - ELEVATOR / ESCALATOR",
      "EXPLOSN",    "EXPLOSION",
      "EYE INJY",   "EYE PROBLEMS / INJURIES",
      "FARM MCH",   "VEHICLE - FARM MACHINERY",
      "FILL IN",    "MUTUAL AID - STATION FILL IN",
      "FIRE XFR",   "FIRE CALL TRANSFER",
      "FLD/WOOD",   "OUTSIDE FIRE - FIELD / WOODS",
      "FUEL SPL",   "HAZMAT - FUEL SPILL",
      "GARG FIR",   "STRUCTURE FIRE - GARAGE / SHED",
      "GAS LEAK",   "HAZMAT - GAS LEAK / RUPTURE",
      "GAS ODOR",   "HAZMAT - GAS ODOR",
      "GRASS FI",   "OUTSIDE FIRE - GRASS / MULCH",
      "HAZMAT",     "HAZMAT - SPILL / C.B.R.N.",
      "HEADACHE",   "HEADACHE",
      "HRT PROB",   "HEART PROBLEMS / A.I.C.D.",
      "INJ PRSN",   "ANIMAL BITES / ATTACKS",
      "INJ PRSN",   "BATTERY / SEXUAL ASSAULT",
      "INJ PRSN",   "FALLS",
      "INJ PRSN",   "HEMORRHAGE / LACERATIONS",
      "INJ PRSN",   "TRAUMATIC INJURIES (SPECIFIC)",
      "INST ALM",   "ALARM - INSTITUTIONAL",
      "INST ALM 1",   "ALARM - INSTITUTIONAL 1",
      "INST ALM 2",   "ALARM - INSTITUTIONAL 2",
      "INST FIR",   "STRUCTURE FIRE - INSTITUTIONAL",
      "INVEST",     "INVESTIGATION - FIRE",
      "LGTNG ST",   "INVESTIGATION - LIGHTNING STRK",
      "LOCKOUT",    "SERVICE CALL - RESD LOCKOUT",
      "MED ALRM",   "MEDICAL ALARM",
      "MED ASST",   "MUTUAL-AID - MEDICAL",
      "METH LAB",   "HAZMAT - METH / DRUG LABS",
      "METNL PT",   "MENTAL EMOTIONAL / SUICIDE ATT",
      "MINOR PI",   "PI ACCIDENT - MINOR RESP",
      "MUTL AID",   "MUTUAL AID - FIRE / HAZMAT",
      "ODOR INV",   "INVESTIGATION - ODOR",
      "OUTSIDE",    "OUTSIDE FIRE",
      "OVERDOSE",   "OVERDOSE / POISONING",
      "PAT ASST",   "PATIENT ASSIST (NO INJURIES)",
      "PAT TRNF",   "PATIENT TRANSFER",
      "PLS ASST",   "MUTUAL AID - PROJECT LIFESAVER",
      "PWR LINE",   "ELECTRICAL - LINES DOWN",
      "RES ALM",    "ALARM - RESIDENTIAL",
      "RES FIRE",   "STRUCTURE FIRE - RESIDENTIAL",
      "RES FIRE 1", "STRUCTURE FIRE - RESIDENTIAL 1",
      "RES FIRE 2", "STRUCTURE FIRE - RESIDENTIAL 2",
      "RESCUE",     "RESCUE - ENTRAPMENT (NON-VEH)",
      "RESD ALM",   "ALARM - RESIDENTIAL",
      "ROPE RES",   "RESCUE - ROPE / ANGLE / TOWER",
      "SEIZURE",    "CONVULSIONS / SEIZURES",
      "SERUS PI",   "PI ACCIDENT - SERIOUS RESP",
      "SICK PSN",   "HEAT / COLD EXPOSURE",
      "SICK PSN",   "SICK PRSN - SPECIFIC DIAGNOSIS",
      "SMK INV",    "INVESTIGATION - SMOKE IN AREA",
      "STROKE",     "STROKE / CVA",
      "SUBJ FIR",   "PERSON ON FIRE",
      "SUSP PKG",   "WMD - SUSPICIOUS ITEM",
      "TRAUMA",     "STAB / GUNSHOT / PEN TRAUMA",
      "TRN ACCD",   "TRAIN - COLLISION / DERAILMENT",
      "TRN FIRE",   "TRAIN - FIRE",
      "TRNSFMR",    "ELECTRICAL - TRANSFORMER FIRE",
      "UNCN PSN",   "UNCONSCIOUS / FAINTING",
      "UNKN MED",   "UNKNOWN MEDICAL PROBLEM",
      "VEH BLDG",   "PI ACCIDENT - VEH INTO BLDG",
      "VEH FIRE",   "VEHICLE - PASSENGER CAR/TRUCK",
      "WTR RESC",   "RESCUE - WATER / ICE"
  });
}
