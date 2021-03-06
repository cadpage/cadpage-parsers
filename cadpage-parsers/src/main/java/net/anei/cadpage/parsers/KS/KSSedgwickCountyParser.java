package net.anei.cadpage.parsers.KS;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA21Parser;


public class KSSedgwickCountyParser extends DispatchA21Parser {
  
  public KSSedgwickCountyParser() {
    super(CITY_CODES, CALL_CODES, "SEDGWICK COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "cad_admin@sedgwick.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    String[] parts= subject.split("\\|");
    if (parts.length != 2 || !parts[0].equals("CommandPoint CAD Message")) return false;
    return super.parseMsg(parts[1], body, data);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = KNN_PTN.matcher(addr).replaceAll("STATE $1");
    return addr;
  }
  private static final Pattern KNN_PTN = Pattern.compile("\\bK(\\d+)\\b");
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "01",      "HOMICIDE",
      "106",     "BUSY",
      "47HZ",    "NON INJURY ACC W/HAZMAT",
      "48",      "INJURY ACCIDENT",
      "48A",     "INJURY ACCIDENT-LEV A",
      "48B",     "INJURY ACCIDENT-LEV B",
      "48C",     "INJURY ACCIDENT-LEV C",
      "48D",     "INJURY ACCIDENT-LEV D",
      "48HR",    "INJURY ACCIDENT HIT & RUN",
      "48HZ",    "INJURY ACCIDENT W/HAZMAT",
      "48T",     "INJURY ACCIDENT-PT PINNED",
      "ABDOA",   "ABDOMINAL PAINS-LEVEL A",
      "ABDOB",   "ABDOMINAL PAINS-LEVEL B",
      "ABDOC",   "ABDOMINAL PAINS-LEVEL C",
      "ABDOD",   "ABDOMINAL PAINS-LEVEL D",
      "ABDOM",   "ABDOMINAL PAINS",
      "ABDUCT",  "ABDUCTION",
      "ALL",     "FALL",
      "ALPHA",   "FIRE ALPHA LEVEL CALL",
      "ALRGA",   "ALLERGIC REACTION-LEVEL A",
      "ALRGB",   "ALLERGIC REACTION-LEVEL B",
      "ALRGC",   "ALLERGIC REACTION-LEVEL C",
      "ALRGD",   "ALLERGIC REACTION-LEVEL D",
      "ALRGE",   "ALLERGIC REACTION-LEVEL E",
      "ALRGY",   "ALLERGIC REACTION",
      "ANBIA",   "ANIMAL BITE-LEVEL A",
      "ANBIB",   "ANIMAL BITE-LEVEL B",
      "ANBIC",   "ANIMAL BITE-LEVEL C",
      "ANBID",   "ANIMAL BITE-LEVEL D",
      "ANBIT",   "ANIMAL BITE",
      "ANBITE",  "ANIMAL BITE",
      "AOR",     "AVAILABLE ON RADIO",
      "APT",     "APARTMENT FIRE",
      "ASLT",    "ASSAULT",
      "ASLTA",   "ASSAULT-LEVEL A",
      "ASLTB",   "ASSAULT-LEVEL B",
      "ASLTC",   "ASSAULT-LEVEL C",
      "ASLTD",   "ASSAULT-LEVEL D",
      "ASSTC",   "ASSIST A CITIZEN",
      "ASSTE",   "ASSIST EMS",
      "ASSTP",   "ASSIST LAW ENFORCEMENT",
      "AUD",     "AUDIBLE ALARM",
      "BACK",    "NON-TRAUMATIC BACK PAINS",
      "BACKA",   "BACK PAINS-LEVEL A",
      "BACKB",   "BACK PAINS-LEVEL B",
      "BACKC",   "BACK PAINS-LEVEL C",
      "BACKD",   "BACK PAINS-LEVEL D",
      "BARN",    "BARN FIRE",
      "BLDG",    "BUILDING FIRE",
      "BLDGH",   "BUILDING FIRE WITH HAZ MAT",
      "BLDGHZ",  "BUILDING FIRE W/HAZMAT",
      "BLDGP",   "BUILDING FIRE WITH PERSON TRAPPED",
      "BLDGPT",  "BUILDING FIRE WITH PERSON TRAPPED",
      "BLDGT",   "BUILDING FIRE WITH PERSON TRAPPED",
      "BLUE",    "POSS CODE BLUE PATIENT",
      "BLUEA",   "CODE BLUE",
      "BLUEB",   "CODE BLUE-LEVEL B",
      "BLUEC",   "CODE BLUE",
      "BLUED",   "POSS CODE BLUE-LEVEL D",
      "BLUEE",   "POSS CODE BLUE-LEVEL E",
      "BRUSH",   "BRUSH FIRE",
      "BURN",    "BURN VICTIM",
      "BURNA",   "BURNS-LEVEL A",
      "BURNB",   "BURNS-LEVEL B",
      "BURNC",   "BURNS-LEVEL C",
      "BURND",   "BURNS-LEVEL D",
      "BURNE",   "BURNS-LEVEL E",
      "BURNS",   "BURNS",
      "BUS",     "SCHOOL/CHARTER BUS FIRE",
      "CAVEI",   "CAVE IN",
      "CAVEIN",  "CAVE IN",
      "CHESA",   "CHEST PAINS-LEVEL A",
      "CHESA",   "CHEST PAINS-LEVEL B",
      "CHESC",   "CHEST PAINS-LEVEL C",
      "CHESD",   "CHEST PAINS-LEVEL D",
      "CHEST",   "CHEST PAINS",
      "CHOKA",   "PERSON CHOKING-LEVEL A",
      "CHOKB",   "PERSON CHOKING-LEVEL B",
      "CHOKC",   "PERSON CHOKING-LEVEL C",
      "CHOKD",   "PERSON CHOKING-LEVEL D",
      "CHOKE",   "PERSON CHOKING",
      "CKAPP",   "CHECK APPLIANCE",
      "CKCLUB",  "CHECK A CLUB",
      "CLUB",    "CLUBBING",
      "CMF",     "FIRE MAINTENANCE",
      "COALM",   "CARBON MONOXIDE ALARM",
      "COD25",   "BARRICADED SUBJECT",
      "COD26",   "BOMB THREAT",
      "CODE25",  "BARRICADED SUBJECT",
      "CODE26",  "BOMB THREAT",
      "COLA1",   "BLDG COLLAPSE-LEVEL 1",
      "COLA2",   "BLDG COLLAPSE-LEVEL 2",
      "COLA3",   "BLDG COLLAPSE-LEVEL 3",
      "COLD",    "COLD EXPOSURE",
      "COLDA",   "COLD EXPOSURE-LEVEL A",
      "COLDB",   "COLD EXPOSURE-LEVEL B",
      "COLDC",   "COLD EXPOSURE-LEVEL C",
      "COLDD",   "COLD EXPOSURE-LEVEL D",
      "CUT",     "CUTTING/STABBING",
      "CUTA",    "CUTTING/STABBING-LEVEL A",
      "CUTB",    "CUTTING/STABBING-LEVEL B",
      "CUTC",    "CUTTING/STABBING-LEVEL C",
      "CUTD",    "CUTTING/STABBING-LEVEL D",
      "CUTTIN",  "CUTTING",
      "CUTTING", "CUTTING",
      "DEATA",   "SUDDEN DEATH-LEVEL A",
      "DEATB",   "SUDDEN DEATH-LEVEL B",
      "DEATC",   "SUDDEN DEATH-LEVEL C",
      "DEATD",   "SUDDEN DEATH-LEVEL D",
      "DEATH",   "SUDDEN DEATH",
      "DERAI",   "TRAIN FIRE/DERAILMENT",
      "DEVICE",  "FOUND EXPLOSIVE DEVICE",
      "DIABA",   "DIABETIC-LEVEL A",
      "DIABB",   "DIABETIC-LEVEL B",
      "DIABC",   "DIABETIC-LEVEL C",
      "DIABD",   "DIABETIC-LEVEL D",
      "DIABE",   "DIABETIC COMPLICATIONS",
      "DIABET",  "DIABETIC COMPLICATIONS",
      "DIFFB",   "DIFFICULTY BREATHING",
      "DIFFBR",  "DIFFICULTY BREATHING",
      "DIFFA",   "DIFF BREATHING-LEVEL A",
      "DIFFB",   "DIFF BREATHING-LEVEL B",
      "DIFFC",   "DIFF BREATHING-LEVEL C",
      "DIFFD",   "DIFF BREATHING-LEVEL D",
      "DIFFE",   "DIFF BREATHING-LEVEL E",
      "DOC",     "DEPT OPER CNTR ACTIVATION",
      "DROWA",   "DROWNING-LEVEL A",
      "DROWB",   "DROWNING-LEVEL B",
      "DROWC",   "DROWNING-LEVEL C",
      "DROWD",   "DROWNING-LEVEL D",
      "DROWN",   "DROWNING IN BATHTUB/POOL",
      "E48",     "INJURY ACCIDENT",
      "E48HR",   "INJURY ACCIDENT HIT & RUN",
      "E48HZ",   "INJURY ACCIDENT W/HAZMAT",
      "E48T",    "INJURY ACCIDENT-PT PINNED",
      "EABDOM",  "ABDOMINAL PAINS",
      "EALRGY",  "ALLERGIC REACTION",
      "EANBIT",  "ANIMAL BITE",
      "EAPTT",   "APT FIRE - SUB TRAPPED",
      "EASLT",   "ASSAULT",
      "EBACK",   "NON-TRAUMATIC BACK PAINS",
      "EBLDGT",  "BUILDING FIRE-SUB TRAPPED",
      "EBURNS",  "BURNS",
      "ECHEST",  "CHEST PAINS",
      "ECHOKE",  "PERSON CHOKING",
      "ECLUB",   "CLUBBING",
      "ECOD25",  "BARRICADED SUBJECT",
      "ECOD26",  "BOMB THREAT",
      "ECOLA2",  "BLDG COLLAPSE-LEVEL 2",
      "ECOLA3",  "BLDG COLLAPSE-LEVEL 3",
      "ECOLD",   "COLD EXPOSURE",
      "ECUT",    "CUTTING/STABBING",
      "EDEATH",  "SUDDEN DEATH",
      "EDEVIC",  "FOUND EXPLOSIVE DEVICE",
      "EDIABE",  "DIABETIC EMERGENCY",
      "EDIFFB",  "DIFFICULTY BREATHING",
      "EDROWN",  "DROWNING IN BATHTUB/POOL",
      "EEYE",    "EYE PROBLEMS",
      "EFALL",   "FALL",
      "EFALLB",  "FALL-LEVEL B",
      "EFIRE",   "APARTMENT FIRE",
      "EGREEN",  "RESPONDER TRIAGE GREEN",
      "EHEAD",   "HEAD INJURY",
      "EHEART",  "HEART PROBLEMS",
      "EHEAT",   "HEAT RELATED EMERGENCY",
      "EHEMOR",  "BLEEDING",
      "EHIRIST", "HIGH RISE FIRE WITH PERSON TRAPPED",
      "EHOUSET", "HOUSE FIRE WITH PERSON TRAPPED",
      "EHZ1",    "HAZ MAT-LEVEL 1",
      "EHZ2",    "HAZ MAT-LEVEL 2",
      "EHZ3",    "HAZ MAT-LEVEL 3",
      "EHZ4",    "HAZ MAT-LEVEL 4",
      "EHZ5",    "HAZ MAT-LEVEL 5",
      "EHZ6",    "HAZ MAT-LEVEL 6",
      "EICTAI",  "AIRPORT EMERGENCY",
      "EINDACC", "INDUSTRIAL ACCIDENT",
      "ELACER",  "LACERATION",
      "ELEV",    "PERSON STUCK IN ELEVATOR",
      "ELOSTA",  "LOST ADULT",
      "ELOSTJ",  "LOST JUVENILE",
      "EMA",     "MEDICAL ALARM",
      "EMANDW",  "MAN DOWN",
      "ENOTBR",  "PATIENT NOT BREATHING",
      "EOB",     "OBSTETRICAL EMERGENCY",
      "EOD",     "OVERDOSE",
      "EPLANE",  "PLANE ACCIDENT",
      "EPOISO",  "POISON",
      "ESEIZU",  "SEIZURE",
      "ESHOCK",  "SHOCK VICTIM",
      "ESHOOT",  "SHOOTING",
      "ESICK",   "SICK PERSON",
      "ESTDBY",  "STAND BY",
      "ESTROKE", "STROKE",
      "ESUB1",   "SUBMERSION - LEVEL 1",
      "ESUB2",   "SUBMERSION - LEVEL 2",
      "ESUB3",   "SUBMERSION - LEVEL 3",
      "ESUB4",   "SUBMERSION - LEVEL 4",
      "ESUICI",  "SUICIDE",
      "ETRAUM",  "TRAUMATIC INJURY",
      "ETRAUMA", "TRAUMATIC INJURY",
      "EUNCOD",  "SUBJECT UNCON-LEVEL D",
      "EUNCON",  "SUBJECT UNCONSCIOUS",
      "EUNKE",   "UNKNOWN CALL FOR EMS",
      "EXPLO",   "CHECK FOR AN EXPLOSION",
      "EXPLOS",  "CHECK FOR AN EXPLOSION",
      "EYE",     "EYE PROBLEMS",
      "EYEA",    "EYE PROBLEMS-LEVEL A",
      "EYEB",    "EYE PROBLEMS-LEVEL B",
      "EYEC",    "EYE PROBLEMS-LEVEL C",
      "EYED",    "EYE PROBLEMS-LEVEL D",
      "F01",     "HOMICIDE",
      "F106",    "BUSY",
      "F47HZ",   "NON INJURY ACC W/HAZMAT",
      "F48",     "INJURY ACCIDENT",
      "F489",    "INJURY ACCIDENT",
      "F48A",    "INJURY ACCIDENT",
      "F48B",    "INJURY ACCIDENT",
      "F48D",    "INJURY ACCIDENT",
      "F48HR",   "INJURY ACCIDENT HIT & RUN",
      "F48HZ",   "INJURY ACCIDENT W/HAZMAT",
      "F48T",    "INJURY ACCIDENT-PT PINNED",
      "FABDOA",  "ABDOMINAL PAINS-LEVEL A",
      "FABDOB",  "ABDOMINAL PAINS-LEVEL B",
      "FABDOC",  "ABDOMINAL PAINS-LEVEL C",
      "FABDOD",  "ABDOMINAL PAINS-LEVEL D",
      "FABDOM",  "ABDOMINAL PAINS",
      "FACTIV",  "ACTIVE SHOOTER",
      "FALL",    "FALL",
      "FALLA",   "FALL-LEVEL A",
      "FALLB",   "FALL-LEVEL B",
      "FALLC",   "FALL-LEVEL C",
      "FALLD",   "FALL-LEVEL D",
      "FALRGA",  "ALLERGIC REACTION-LEVEL A",
      "FALRGB",  "ALLERGIC REACTION-LEVEL B",
      "FALRGC",  "ALLERGIC REACTION-LEVEL D",
      "FALRGD",  "ALLERGIC REACTION-LEVEL D",
      "FALRGE",  "ALLERGIC REACTION-LEVEL E",
      "FALRGY",  "ALLERGIC REACTION",
      "FANBIA",  "ANIMAL BITE-LEVEL A",
      "FANBIB",  "ANIMAL BITE-LEVEL B",
      "FANBIC",  "ANIMAL BITE-LEVEL C",
      "FANBID",  "ANIMAL BITE-LEVEL D",
      "FANBIT",  "ANIMAL BITE",
      "FAOR",    "AVAILABLE ON RADIO",
      "FAPTT",   "APT FIRE - SUB TRAPPED",
      "FASLT",   "ASSAULT",
      "FASLTA",  "ASSAULT-LEVEL A",
      "FASLTB",  "ASSAULT-LEVEL B",
      "FASLTD",  "ASSAULT-LEVEL D",
      "FASLTD",  "ASSAULT-LEVEL D",
      "FASSTC",  "ASSIST A CITIZEN",
      "FASSTE",  "ASSIST EMS",
      "FBACK",   "BACK PAINS-LEVEL A",
      "FBACKA",  "BACK PAINS-LEVEL B",
      "FBACKC",  "BACK PAINS-LEVEL C",
      "FBACKD",  "BACK PAINS-LEVEL D",
      "FBC",     "WRONG WAY DRIVER",
      "FBLDGT",  "BUILDING FIRE-SUB TRAPPED",
      "FBURN3",  "BURNS-LEVEL 3",
      "FBURN5",  "BURNS-LEVEL 5",
      "FBURNA",  "BURNS-LEVEL A",
      "FBURNB",  "BURNS-LEVEL B",
      "FBURNC",  "BURNS-LEVEL C",
      "FBURND",  "BURNS-LEVEL D",
      "FBURNE",  "BURNS-LEVEL E",
      "FBURNS",  "BURNS",
      "FBURNS",  "BURNS",
      "FCC",     "FIREWORKS COMPLAINT",
      "FCCNO",   "FIREWORKS-NO COMPLAINT",
      "FCHESA",  "CHEST PAINS-LEVEL A",
      "FCHESB",  "CHEST PAINS-LEVEL B",
      "FCHESC",  "CHEST PAINS-LEVEL C",
      "FCHESD",  "CHEST PAINS-LEVEL D",
      "FCHEST",  "CHEST PAINS",
      "FCHOK1",  "PERSON CHOKING",
      "FCHOKA",  "PERSON CHOKING-LEVEL A",
      "FCHOKB",  "PERSON CHOKING-LEVEL B",
      "FCHOKC",  "PERSON CHOKING-LEVEL C",
      "FCHOKD",  "PERSON CHOKING-LEVEL D",
      "FCHOKE",  "PERSON CHOKING-LEVEL E",
      "FCLUB",   "CLUBBING",
      "FCMF",    "FIRE MAINTENANCE",
      "FCOD25",  "BARRICADED SUBJECT",
      "FCOD26",  "BOMB THREAT",
      "FCOLA1",  "BLDG COLLAPSE-LEVEL 1",
      "FCOLA2",  "BLDG COLLAPSE-LEVEL 2",
      "FCOLA3",  "BLDG COLLAPSE-LEVEL 3",
      "FCOLD",   "COLD EXPOSURE",
      "FCOLDA",  "COLD EXPOSURE-LEVEL A",
      "FCOLDB",  "COLD EXPOSURE-LEVEL B",
      "FCOLDC",  "COLD EXPOSURE-LEVEL C",
      "FCOLDD",  "COLD EXPOSURE-LEVEL D",
      "FCUT",    "CUTTING/STABBING",
      "FCUT3",   "CUTTING/STABBING-LEVEL 3",
      "FCUTA",   "CUTTING/STABBING-LEVEL A",
      "FCUTB",   "CUTTING/STABBING-LEVEL B",
      "FCUTC",   "CUTTING/STABBING-LEVEL C",
      "FCUTD",   "CUTTING/STABBING-LEVEL D",
      "FDEATB",  "NOT BREATHING",
      "FDEATH",  "NOT BREATHING",
      "FDERAI",  "TRAIN FIRE/DERAILMENT",
      "FDERAIL", "TRAIN FIRE/DERAILMENT",
      "FDEVIC",  "FOUND EXPLOSIVE DEVICE",
      "FDIABA",  "DIABETIC COMPLICATIONS-LEVEL A",
      "FDIABB",  "DIABETIC COMPLICATIONS-LEVEL B",
      "FDIABC",  "DIABETIC COMPLICATIONS-LEVEL C",
      "FDIABD",  "DIABETIC COMPLICATIONS-LEVEL D",
      "FDIABE",  "DIABETIC COMPLICATIONS-LEVEL E",
      "FDIFFA",  "DIFFICULTY BREATHING-LEVEL A",
      "FDIFFB",  "DIFFICULTY BREATHING-LEVEL B",
      "FDIFFC",  "DIFFICULTY BREATHING-LEVEL C",
      "FDIFFD",  "DIFFICULTY BREATHING-LEVEL D",
      "FDIFFE",  "DIFFICULTY BREATHING-LEVEL E",
      "FDROWA",  "DROWNING-LEVEL A",
      "FDROWB",  "DROWNING-LEVEL B",
      "FDROWC",  "DROWNING-LEVEL C",
      "FDROWD",  "DROWNING-LEVEL D",
      "FDROWN",  "DROWNING",
      "FELEC1",  "ELECTROCUTION-LEVEL 1",
      "FELEC5",  "ELECTROCUTION-LEVEL 5",
      "FELECT",  "ELECTROCUTION",
      "FEXPLO",  "CHECK FOR AN EXPLOSION",
      "FEYE",    "EYE PROBLEMS",
      "FEYEA",   "EYE PROBLEMS-LEVEL A",
      "FEYEB",   "EYE PROBLEMS-LEVEL B",
      "FEYEC",   "EYE PROBLEMS-LEVEL C",
      "FEYED",   "EYE PROBLEMS-LEVEL D",
      "FFALL",   "FALL",
      "FFALL6",  "FALL-LEVEL 6",
      "FFALLA",  "FALL-LEVEL A",
      "FFALLB",  "FALL-LEVEL B",
      "FFALLC",  "FALL-LEVEL C",
      "FFALLD",  "FALL-LEVEL D",
      "FFCC",    "FIREWORKS COMPLAINT",
      "FFCCNO",  "FIREWORKS-NO COMPLAINT",
      "FFLU",    "PANDEMIC FLU",
      "FFLUA",   "PANDEMIC FLU-LEVEL A",
      "FFLUB",   "PANDEMIC FLU-LEVEL B",
      "FFLUC",   "PANDEMIC FLU-LEVEL C",
      "FFLUD",   "PANDEMIC FLU-LEVEL D",
      "FFLUO",   "PANDEMIC FLU-LEVEL O",
      "FFUP",    "FOLLOW UP WORK",
      "FGREEN",  "RESPONDER TRIAGE GREEN",
      "FHEAD",   "HEAD INJURY",
      "FHEADA",  "HEADACHE-LEVEL A",
      "FHEADB",  "HEADACHE-LEVEL B",
      "FHEADC",  "HEADACHE-LEVEL C",
      "FHEADD",  "HEADACHE-LEVEL D",
      "FHEARA",  "HEART PROBLEMS-LEVEL A",
      "FHEARB",  "HEART PROBLEMS-LEVEL B",
      "FHEARC",  "HEART PROBLEMS-LEVEL C",
      "FHEARD",  "HEART PROBLEMS-LEVEL D",
      "FHEART",  "HEART PROBLEMS",
      "FHEAT",   "HEAT RELATED EMERGENCY",
      "FHEATA",  "HEAT EXPOSURE-LEVEL A",
      "FHEATB",  "HEAT EXPOSURE-LEVEL B",
      "FHEATC",  "HEAT EXPOSURE-LEVEL C",
      "FHEATD",  "HEAT EXPOSURE-LEVEL D",
      "FHEMOA",  "BLEEDING-LEVEL A",
      "FHEMOB",  "BLEEDING-LEVEL B",
      "FHEMOC",  "BLEEDING-LEVEL C",
      "FHEMOD",  "BLEEDING-LEVEL D",
      "FHEMOR",  "BLEEDING",
      "FHIRIST", "HIGH RISE FIRE WITH PERSON TRAPPED",
      "FHIRIT",  "HIGH RISE FIRE WITH PERSON TRAPPED",
      "FHOSP",   "HOSPITAL",
      "FHOUSET", "HOUSE FIRE WITH PERSON TRAPPED",
      "FHOUST",  "HOUSE FIRE WITH PERSON TRAPPED",
      "FHTCDA",  "HOT/COLD EXPOSURE-LEVEL A",
      "FHTCDB",  "HOT/COLD EXPOSURE-LEVEL B",
      "FHTCDC",  "HOT/COLD EXPOSURE-LEVEL C",
      "FHTCDD",  "HOT/COLD EXPOSURE-LEVEL D",
      "FHZ2",    "CHEM/RADIOACTIV SPIL/LEAK-LEVEL 2",
      "FHZ3",    "CHEM/RADIOACTIV SPIL/LEAK-LEVEL 3",
      "FHZ6",    "UNKNOWN WHITE POWDER",
      "FHZA",    "CO/INHALATION/HZMAT-LEVEL A",
      "FHZB",    "CO/INHALATION/HZMAT-LEVEL B",
      "FHZC",    "CO/INHALATION/HZMAT-LEVEL C",
      "FHZD",    "CO/INHALATION/HZMAT-LEVEL D",
      "FICTAI",  "AIRPORT EMERGENCY",
      "FINDAA",  "INDUSTRIAL ACCIDENT-LEVEL A",
      "FINDAB",  "INDUSTRIAL ACCIDENT-LEVEL B",
      "FINDAC",  "INDUSTRIAL ACCIDENT-LEVEL C",
      "FINDACC", "INDUSTRIAL ACCIDENT",
      "FINDAD",  "INDUSTRIAL ACCIDENT-LEVEL D",
      "FINHAL",  "INHALATION",
      "FINHL1",  "INHALATION-LEVEL 1",
      "FINHL3",  "INHALATION-LEVEL 3",
      "FINHL6",  "INHALATION-LEVEL 6",
      "FINSP",   "INSPECTION",
      "FIROUT",  "CHECK A FIRE THAT IS OUT",
      "FJUMP5",  "JUMPER",
      "FJUMPR",  "JUMPER",
      "FLACER",  "LACERATION",
      "FLOCKE",  "PERSON/ANIMAL LOCK IN/OUT",
      "FLOSTA",  "LOST ADULT",
      "FLOSTJ",  "LOST JUVENILE",
      "FLU",     "PANDEMIC FLU",
      "FLUA",    "PANDEMIC FLU - LEVEL A",
      "FLUB",    "PANDEMIC FLU - LEVEL B",
      "FLUC",    "PANDEMIC FLU - LEVEL C",
      "FLUD",    "PANDEMIC FLU - LEVEL D",
      "FLUNCH",  "LUNCH",
      "FLUO",    "PANDEMIC FLU - LEVEL O",
      "FMA",     "MEDICAL ALARM",
      "FMANDN",  "FIRE CHECK A MAN DOWN",
      "FMANDW",  "MAN DOWN",
      "FMETH",   "METH LAB",
      "FNOTB1",  "NOT BREATHING",
      "FNOTBR",  "NOT BREATHING",
      "FOB",     "OBSTETRICAL EMERGENCY",
      "FOBA",    "OBSTETRICAL EMERGENCY-LEVEL A",
      "FOBB",    "OBSTETRICAL EMERGENCY-LEVEL B",
      "FOBC",    "OBSTETRICAL EMERGENCY-LEVEL C",
      "FOBD",    "OBSTETRICAL EMERGENCY-LEVEL D",
      "FOBO",    "OBSTETRICAL EMERGENCY",
      "FOD",     "OVERDOSE",
      "FOD5",    "OVERDOSE-LEVEL 5",
      "FODA",    "OVERDOSE-LEVEL A",
      "FODB",    "OVERDOSE-LEVEL B",
      "FODC",    "OVERDOSE-LEVEL C",
      "FODD",    "OVERDOSE-LEVEL D",
      "FOMEGA",  "OMEGA CALL",
      "FOT",     "OFF TRACK",
      "FPAGER",  "OUT ON PAGER",
      "FPE",     "PHYSICAL EDUCATION",
      "FPLANE",  "AIRPLANE EMERG OR CRASH",
      "FPOISO",  "POISON",
      "FPSYC3",  "PSYCHIATRIC CALL-LEVEL 3",
      "FPSYC5",  "PSYCHIATRIC CALL-LEVEL 5",
      "FPSYC9",  "PSYCHIATRIC CALL-LEVEL 9",
      "FPSYCH",  "PSYCHIATRIC CALL",
      "FSA",     "SPECIAL ASSIGNMENT",
      "FSEIZA",  "SEIZURE-LEVEL A",
      "FSEIZB",  "SEIZURE-LEVEL B",
      "FSEIZC",  "SEIZURE-LEVEL C",
      "FSEIZD",  "SEIZURE-LEVEL D",
      "FSEIZU",  "SEIZURE",
      "FSHOCA",  "ELECTROCUTION-LEVEL A",
      "FSHOCB",  "ELECTROCUTION-LEVEL B",
      "FSHOCC",  "ELECTROCUTION-LEVEL C",
      "FSHOCD",  "ELECTROCUTION-LEVEL D",
      "FSHOCE",  "ELECTROCUTION-LEVEL E",
      "FSHOCK",  "ELECTROCUTION",
      "FSHOO3",  "SHOOTING-LEVEL 3",
      "FSHOOA",  "SHOOTING-LEVEL A",
      "FSHOOB",  "SHOOTING-LEVEL B",
      "FSHOOC",  "SHOOTING-LEVEL C",
      "FSHOOD",  "SHOOTING-LEVEL D",
      "FSHOOT",  "SHOOTING",
      "FSICK",   "SICK PERSON",
      "FSICK6",  "SICK PERSON-LEVEL 6",
      "FSICKA",  "SICK PERSON-LEVEL A",
      "FSICKB",  "SICK PERSON-LEVEL B",
      "FSICKC",  "SICK PERSON-LEVEL C",
      "FSICKD",  "SICK PERSON-LEVEL D",
      "FSICKO",  "SICK PERSON-LEVEL O",
      "FSTDBY",  "STAND BY",
      "FSTRO1",  "STROKE-LEVEL 1",
      "FSTROA",  "STROKE-LEVEL A",
      "FSTROB",  "STROKE-LEVEL B",
      "FSTROC",  "STROKE-LEVEL C",
      "FSTROC",  "STROKE-LEVEL D",
      "FSTROK",  "STROKE",
      "FSTROKE", "STROKE-LEVEL E",
      "FSUB",    "SUBMERSION",
      "FSUB1",   "SUBMERSION-LEVEL 1",
      "FSUB2",   "SUBMERSION-LEVEL 2",
      "FSUB3",   "SUBMERSION-LEVEL 3",
      "FSUIC3",  "SUICIDE-LEVEL 3",
      "FSUIC9",  "SUICIDE-LEVEL 9",
      "FSUICA",  "SUICIDE-LEVEL A",
      "FSUICB",  "SUICIDE-LEVEL B",
      "FSUICC",  "SUICIDE-LEVEL C",
      "FSUICD",  "SUICIDE-LEVEL D",
      "FSUICI",  "SUICIDE",
      "FSYCH1",  "PSYCH CALL",
      "FTEST",   "TEST CALL FOR FIRE",
      "FTRAIN",  "TRAINING",
      "FTRAU3",  "TRAUMATIC INJURY-LEVEL 3",
      "FTRAU6",  "TRAUMATIC INJURY-LEVEL 6",
      "FTRAUA",  "TRAUMATIC INJURY-LEVEL A",
      "FTRAUB",  "TRAUMATIC INJURY-LEVEL B",
      "FTRAUC",  "TRAUMATIC INJURY-LEVEL C",
      "FTRAUD",  "TRAUMATIC INJURY-LEVEL D",
      "FTRAUM",  "TRAUMATIC INJURY",
      "FTRAUMA", "TRAUMATIC INJURY",
      "FUNCO1",  "SUBJECT UNCONSCIOUS-LEVEL 1",
      "FUNCO2",  "SUBJECT UNCONSCIOUS-LEVEL 2",
      "FUNCOA",  "SUBJECT UNCONSCIOUS-LEVEL A",
      "FUNCOB",  "SUBJECT UNCONSCIOUS-LEVEL B",
      "FUNCOC",  "SUBJECT UNCONSCIOUS-LEVEL C",
      "FUNCOD",  "SUBJECT UNCONSCIOUS-LEVEL D",
      "FUNCOE",  "SUBJECT UNCONSCIOUS-LEVEL E",
      "FUNCON",  "SUBJECT UNCONSCIOUS",
      "FUNKE",   "UNKNOWN CALL FOR EMS",
      "FUNKE9",  "UNKNOWN CALL FOR EMS-LEVEL 9",
      "FUNKEA",  "UNKNOWN CALL FOR EMS-LEVEL A",
      "FUNKEB",  "UNKNOWN CALL FOR EMS-LEVEL B",
      "FUNKEC",  "UNKNOWN CALL FOR EMS-LEVEL C",
      "FUNKED",  "UNKNOWN CALL FOR EMS-LEVEL D",
      "FUP",     "FOLLOW UP WORK",
      "FVACAT",  "VAC LOG FOR INDIVIDUAL",
      "FVEH",    "VEHICLE FIRE",
      "FWRONG",  "WRONG WAY DRIVER",
      "FXFER",   "FIRE TRANSFER",
      "FXFER1",  "XFER CALL-LEVEL 1",
      "FXFER5",  "XFER CALL-LEVEL 5",
      "FXFER7",  "XFER CALL-LEVEL 7",
      "FXFER8",  "XFER CALL-LEVEL 8",
      "GARAG",   "NON ATTACHED GARAGE FIRE",
      "GARAGE",  "NON ATTACHED GARAGE FIRE",
      "GASIN",   "CK FOR GAS ODOR IN A BLDG",
      "GASOUT",  "CK FOR GAS LEAK OUTSIDE",
      "GRASS1",  "GRASS FIRE - LEVEL 1",
      "GRASS2",  "GRASS FIRE - LEVEL 2",
      "GRASS3",  "GRASS FIRE - LEVEL 3",
      "GREEN",   "RESPONDER TRIAGE GREEN",
      "HEAD",    "HEAD INJURY",
      "HEADA",   "HEADACHE-LEVEL A",
      "HEADB",   "HEADACHE-LEVEL B",
      "HEADC",   "HEADACHE-LEVEL C",
      "HEADD",   "HEADACHE-LEVEL D",
      "HEARA",   "HEART PROBLEMS-LEVEL A",
      "HEARB",   "HEART PROBLEMS-LEVEL B",
      "HEARC",   "HEART PROBLEMS-LEVEL C",
      "HEARD",   "HEART PROBLEMS-LEVEL D",
      "HEART",   "HEART PROBLEMS",
      "HEAT",    "HEAT RELATED EMERGENCY",
      "HEATA",   "HEAT EXPOSURE-LEVEL A",
      "HEATB",   "HEAT EXPOSURE-LEVEL B",
      "HEATC",   "HEAT EXPOSURE-LEVEL C",
      "HEATD",   "HEAT EXPOSURE-LEVEL D",
      "HEMOA",   "BLEEDING-LEVEL A",
      "HEMOB",   "BLEEDING-LEVEL B",
      "HEMOC",   "BLEEDING-LEVEL C",
      "HEMOD",   "BLEEDING-LEVEL D",
      "HEMOR",   "BLEEDING",
      "HIRIS",   "HIGH RISE BUILDING FIRE",
      "HIRISE",  "HIGH RISE BUILDING FIRE",
      "HIRIST",  "HI RISE FIRE SUBJ TRAPPED",
      "HIRIT",   "HI RISE FIRE SUBJ TRAPPED",
      "HOSP",    "HOSPITAL",
      "HOUSE",   "HOUSE FIRE",
      "HOUSET",  "HOUSE FIRE WITH PERSON TRAPPED",
      "HOUST",   "HOUSE FIRE-SUBJ TRAPPED",
      "HTCDA",   "HOT/COLD EXPOSURE-LEVEL A",
      "HTCDB",   "HOT/COLD EXPOSURE-LEVEL B",
      "HTCDC",   "HOT/COLD EXPOSURE-LEVEL C",
      "HTCDD",   "HOT/COLD EXPOSURE-LEVEL D",
      "HZ1",     "KNOWN SUBSTANCE SPILL",
      "HZ2",     "MEDIUM CHEMICAL/RADIOACTIVE SPILL/LEAK",
      "HZ3",     "HAZ MAT LEVEL 3",
      "HZ6",     "HAZ MAT LEVEL 6",
      "HZB",     "CO/INHALATION/HZ-LEVEL B",
      "HZC",     "CO/INHALATION/HZ-LEVEL C",
      "HZD",     "CO/INHALATION/HZ-LEVEL D",
      "HZSM",    "CHEMICAL ODOR CHECK",
      "ICTAI",   "AIRPORT EMERGENCY",
      "ICTAIR",  "AIRPORT EMERGENCY",
      "INDAA",   "INDUSTRIAL ACCIDENT-LEV A",
      "INDAB",   "INDUSTRIAL ACCIDENT-LEV B",
      "INDAC",   "INDUSTRIAL ACCIDENT-LEV C",
      "INDACC",  "INDUSTRIAL ACCIDENT",
      "INDAD",   "INDUSTRIAL ACCIDENT-LEV D",
      "INSP",    "INSPECTION",
      "IROUT",   "CHECK A FIRE THAT IS OUT",
      "LACER",   "LACERATION",
      "LINES",   "LINES DOWN",
      "LOCKED",  "PERSON/ANIMAL LOCK IN/OUT",
      "LOSTA",   "LOST ADULT",
      "LOSTJ",   "LOST JUVENILE",
      "LUNCH",   "LUNCH",
      "MA",      "MEDICAL ALARM",
      "MANDN",   "MAN DOWN",
      "MANDW",   "MAN DOWN",
      "METH",    "METH LAB",
      "MISCF",   "MISC SERVICE",
      "MSGF",    "PHONE MESSAGE",
      "MSGP",    "MESSAGE FOR LAW ENFORCEMENT",
      "MUTUAL",  "AUTOMATIC/MUTUAL AID REQ",
      "NOTBR",   "PATIENT NOT BREATHING",
      "OB",      "OBSTETRICAL EMERGENCY",
      "OBA",     "OBSTETRICAL EMERGENCY-LEVEL A",
      "OBB",     "OBSTETRICAL EMERGENCY-LEVEL B",
      "OBC",     "OBSTETRICAL EMERGENCY-LEVEL C",
      "OBD",     "OBSTETRICAL EMERGENCY-LEVEL D",
      "OD",      "OVERDOSE",
      "ODA",     "OVERDOSE-LEVEL A",
      "ODB",     "OVERDOSE-LEVEL B",
      "ODC",     "OVERDOSE-LEVEL C",
      "ODD",     "OVERDOSE-LEVEL D",
      "OMEGA",   "CANCEL RESP, OMEGA CALL",
      "OT",      "OFF TRACK",
      "OXY",     "BLDG FIRE OXY CHEM",
      "PAGER",   "OUT ON PAGER",
      "PE",      "PHYSICAL EDUCATION",
      "PERMIT",  "ACTIVE BURN PERMIT",
      "PLANE",   "PLANE ACCIDENT",
      "POISO",   "POISON",
      "POISON",  "POISON",
      "POLE",    "POLE ON FIRE",
      "RV",      "RECREATIONAL VEHICLE FIRE",
      "SA",      "SYSTEM ALARM",
      "SC",      "SUSPICIOUS CHARACTER",
      "SEIZA",   "SEIZURE-LEV A",
      "SEIZB",   "SEIZURE-LEV B",
      "SEIZC",   "SEIZURE-LEV C",
      "SEIZD",   "SEIZURE-LEV D",
      "SEIZU",   "SEIZURE",
      "SEIZUR",  "SEIZURE",
      "SEMI",    "TRACTOR TRAILER RIG FIRE",
      "SHED",    "SMALL OUT BLDG FIRE",
      "SHOCC",   "ELECTROCUTION-LEVEL A",
      "SHOCC",   "ELECTROCUTION-LEVEL B",
      "SHOCC",   "ELECTROCUTION-LEVEL C",
      "SHOCD",   "ELECTROCUTION-LEVEL D",
      "SHOCE",   "ELECTROCUTION-LEVEL E",
      "SHOCK",   "SHOCK VICTIM",
      "SHOOA",   "SHOOTING-LEV A",
      "SHOOB",   "SHOOTING-LEV B",
      "SHOOC",   "SHOOTING-LEV C",
      "SHOOD",   "SHOOTING-LEV D",
      "SHOOT",   "SHOOTING",
      "SICK",    "SICK PERSON",
      "SICKA",   "SICK PERSON-LEVEL A",
      "SICKB",   "SICK PERSON-LEVEL B",
      "SICKC",   "SICK PERSON-LEVEL C",
      "SICKD",   "SICK PERSON-LEVEL D",
      "SICKO",   "SICK PERSON-LEVEL O",
      "SMKDET",  "CHECK A SMOKE DETECTOR",
      "SMKIN",   "CK SMOKE IN A BUILDING",
      "SMKOUT",  "CK SMOKE IN OUTSIDE AREA",
      "STDBY",   "STAND BY",
      "STORM",   "CKRES DUE TO WEATHER",
      "STROA",   "STROKE-LEVEL A",
      "STROB",   "STROKE-LEVEL B",
      "STROC",   "STROKE-LEVEL C",
      "STROK",   "STROKE",
      "STROKE",  "STROKE",
      "SUB",     "SUBMERSION",
      "SUB1",    "SUBMERSION-LEVEL 1",
      "SUB2",    "SUBMERSION-LEVEL 2",
      "SUB3",    "SUBMERSION-LEVEL 3",
      "SUICA",   "SUICIDE-LEVEL A",
      "SUICB",   "SUICIDE-LEVEL B",
      "SUICC",   "SUICIDE-LEVEL C",
      "SUICD",   "SUICIDE-LEVEL D",
      "SUICI",   "SUICIDE",
      "SUICID",  "SUICIDE",
      "SYSB",    "SYSTEMS ALARM ON BUSINESS",
      "SYSH",    "SYSTEMS ALARM ON HOSPITAL",
      "SYSR",    "SYSTEMS ALARM ON RESIDENC",
      "TEST",    "TEST CALL FOR FIRE",
      "TRAFIC",  "TRAFFIC INCIDENT",
      "TRAIN",   "TRAIN FIRE",
      "TRAP",    "PERSON TRAPPED",
      "TRASH",   "TRASH FIRE",
      "TRAUA",   "TRAUMATIC INJURY-LEVEL A",
      "TRAUB",   "TRAUMATIC INJURY-LEVEL B",
      "TRAUC",   "TRAUMATIC INJURY-LEVEL C",
      "TRAUD",   "TRAUMATIC INJURY-LEVEL D",
      "TRAUM",   "TRAUMATIC INJURY",
      "TRAUMA",  "TRAUMATIC INJURY",
      "UNCOA",   "SUBJECT UNCON-LEVEL A",
      "UNCOB",   "SUBJECT UNCON-LEVEL B",
      "UNCOC",   "SUBJECT UNCON-LEVEL C",
      "UNCOD",   "SUBJECT UNCON-LEVEL D",
      "UNCOE",   "SUBJECT UNCON-LEVEL E",
      "UNCON",   "SUBJECT UNCONSCIOUS",
      "UNKE",    "UNKNOWN EMERGENCY",
      "UNKEA",   "UNKNOWN EMS-LEVEL A",
      "UNKEB",   "UNKNOWN EMS-LEVEL B",
      "UNKEC",   "UNKNOWN EMS-LEVEL C",
      "UNKED",   "UNKNOWN EMS-LEVEL D",
      "UNKF",    "UNKNOWN FIRE",
      "VACAT",   "VAC LOG FOR INDIVIDUAL",
      "VEH",     "VEHICLE FIRE",
      "WATER",   "WATER RELATED ISSUES/PROB",
      "WHEAT",   "STANDING WHEATFIELD FIRE",
      "WIRING",  "CHECK ELECTRICAL WIRING",
      "WORKIN",  "WORKIN",
      "XFER",    "EMERGENCY MEDICAL TRANSFER - NON EMERGENCY"
  });
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AN", "ANDALE",
      "BA", "BEL AIRE",
      "BE", "BENTLEY",
      "CH", "CHENEY",
      "CL", "CLEARWATER",
      "CO", "COLWICH",
      "DE", "DERBY",
      "EB", "EASTBOROUGH",
      "FU", "FURLEY",
      "GO", "GODDARD",
      "GP", "GARDEN PLAIN",
      "GR", "GREENWICH",
      "HA", "HAYSVILLE",
      "KE", "KECHI",
      "MA", "MAIZE",
      "MC", "MCCONNELL",
      "MH", "MT HOPE",
      "MU", "MULVANE",
      "PC", "PARK CITY",
      "PE", "PECK",
      "SC", "SEDGWICK COUNTY",
      "SH", "SCHULTE",
      "VC", "VALLEY CENTER",
      "VI", "VIOLA",
      "WI", "WICHITA",

  });
}
