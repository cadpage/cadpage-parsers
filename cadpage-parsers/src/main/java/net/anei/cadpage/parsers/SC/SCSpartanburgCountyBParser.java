package net.anei.cadpage.parsers.SC;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA52Parser;

public class SCSpartanburgCountyBParser extends DispatchA52Parser {

  public SCSpartanburgCountyBParser() {
    super(CALL_CODES, "SPARTANBURG COUNTY", "SC", ZIP_CITY_TABLE);
  }

  private static final Properties ZIP_CITY_TABLE = buildCodeTable(new String[] {
      "29301", "Spartanburg",
      "29302", "Spartanburg",
      "29303", "Spartanburg",
      "29306", "Spartanburg",
      "29307", "Spartanburg",
      "29316", "Boiling Springs",
      "29320", "Arcadia",
      "29322", "Campobello",
      "29323", "Chesnee",
      "29324", "Clifton",
      "29329", "Converse",
      "29330", "Cowpens",
      "29331", "Cross Anchor",
      "29333", "Drayton",
      "29334", "Duncan",
      "29335", "Enoree",
      "29338", "Fingerville",
      "29346", "Glendale",
      "29349", "Inman",
      "29356", "Landrum",
      "29365", "Lyman",
      "29368", "Mayo",
      "29369", "Moore",
      "29372", "Pacolet",
      "29373", "Pacolet",
      "29374", "Pauline",
      "29375", "Reidville",
      "29376", "Roebuck",
      "29377", "Startex",
      "29378", "Saxon",
      "29385", "Wellford",
      "29388", "Woodruff"
  });

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "00",       "OFFICER DOWN",
      "00F",      "OFFICER DOWN",
      "00M",      "OFFICER DOWN MEDICAL CALL",
      "50",       "ACCIDENT NO INJURIES",
      "50F",      "NON INJURY VEHICLE ACCIDENT",
      "50L",      "MVA",
      "50PIF",    "MVA WITH INJURIES",
      "50PIL",    "MVA WITH INJURY",
      "50PIM",    "MVA WITH INJURIES MEDICAL",
      "62L",      "CHECK AREA",
      "A1F",      "ALERT 1 GSP STANDBY",
      "A2F",      "ALERT 2 AIRCRAFT NO CRASH",
      "A2L",      "ALERT 2 AIRCRAFT NO CRASH",
      "A2M",      "ALERT 2 AIRCRAFT NO CRASH",
      "A3F",      "ALERT 3 AIRCRAFT CRASH",
      "A3L",      "ALERT 3 AIRCRAFT CRASH",
      "A3M",      "ALERT 3 AIRCRAFT CRASH",
      "AAMBF",    "ASSIST AMBULANCE",
      "AAMBFA",   "ASSIST AMBULANCE",
      "AAMBFB",   "ASSIST AMBULANCE",
      "AAMBFC",   "ASSIST AMBULANCE",
      "AAMBFD",   "ASSIST AMBULANCE",
      "AAMBFE",   "ASSIST AMBULANCE",
      "AAMBL",    "ASSIST AN AMBULANCE",
      "AAMBM",    "ASSIST AN AMBULANCE",
      "AAMBMA",   "ASSIST AN AMBULANCE",
      "AAMBMB",   "ASSIST AN AMBULANCE",
      "AAMBMC",   "ASSIST AN AMBULANCE",
      "AAMBMD",   "ASSIST AN AMBULANCE",
      "AAMBME",   "ASSIST AN AMBULANCE",
      "ABDMF",    "ABDOMINAL PAINS",
      "ABDMFA",   "ABDOMINAL PAINS",
      "ABDMFB",   "ABDOMINAL PAINS",
      "ABDMFC",   "ABDOMINAL PAINS",
      "ABDMFD",   "ABDOMINAL PAINS",
      "ABDMFE",   "ABDOMINAL PAINS",
      "ABDMM",    "ABDOMINAL PAINS",
      "ABDMMA",   "ABDOMINAL PAINS",
      "ABDMMB",   "ABDOMINAL PAINS",
      "ABDMMC",   "ABDOMINAL PAINS",
      "ABDMMD",   "ABDOMINAL PAINS",
      "ABDMME",   "ABDOMINAL PAINS",
      "ABNCH",    "ABANDONED CHILD",
      "ABUSE",    "ABUSE /  CHILD OR ADULT",
      "AGCOMF",   "FIRE AGENCY COMPLAINT",
      "AGCOML",   "AGENCY COMPLAINT",
      "AGCOMM",   "EMS AGENCY COMPLAINT",
      "ALARML",   "ALARM CALLS LAW",
      "ALARMM",   "MEDICAL ALARM",
      "ALARMMA",  "MEDICAL ALARM",
      "ALARMMB",  "MEDICAL ALARM",
      "ALARMMC",  "MEDICAL ALARM",
      "ALARMMD",  "MEDICAL ALARM",
      "ALARMME",  "MEDICAL ALARM",
      "ALARMMF",  "MEDICAL ALARM",
      "ALARMMFA", "MEDICAL ALARM",
      "ALARMMFB", "MEDICAL ALARM",
      "ALARMMFC", "MEDICAL ALARM",
      "ALARMMFD", "MEDICAL ALARM",
      "ALARMMFE", "MEDICAL ALARM",
      "ALRGF",    "ALLERGIC  REACTION",
      "ALRGFA",   "ALLERGIC  REACTION",
      "ALRGFB",   "ALLERGIC  REACTION",
      "ALRGFC",   "ALLERGIC  REACTION",
      "ALRGFD",   "ALLERGIC  REACTION",
      "ALRGFE",   "ALLERGIC  REACTION",
      "ALRGM",    "ALLERGIC REACTION",
      "ALRGMA",   "ALLERGIC REACTION",
      "ALRGMB",   "ALLERGIC REACTION",
      "ALRGMC",   "ALLERGIC REACTION",
      "ALRGMD",   "ALLERGIC REACTION",
      "ALRGME",   "ALLERGIC REACTION",
      "ALRMB",    "ALARM AT A BUISNESS",
      "ALRMBF",   "BUISNESS FIRE ALARM",
      "ALRMF",    "FIRE ALARM",
      "ALRMR",    "ALARM AT A RESIDENCE",
      "ALRMRF",   "RESIDENTIAL FIRE ALARM",
      "AMBER",    "AMBER ALERT",
      "ANBIF",    "ANIMAL BITE",
      "ANBIFA",   "ANIMAL BITE",
      "ANBIFB",   "ANIMAL BITE",
      "ANBIFC",   "ANIMAL BITE",
      "ANBIFD",   "ANIMAL BITE",
      "ANBIFE",   "ANIMAL BITE",
      "ANBIL",    "ANIMAL BITE",
      "ANBIM",    "ANIMAL BITE",
      "ANBIMA",   "ANIMAL BITE",
      "ANBIMB",   "ANIMAL BITE",
      "ANBIMC",   "ANIMAL BITE",
      "ANBIMD",   "ANIMAL BITE",
      "ANBIME",   "ANIMAL BITE",
      "ANIML",    "ANIMAL COMPLAINT",
      "ARSONL",   "ARSON",
      "AS",       "ASSAULT",
      "ASCITF",   "ASSIST A CITIZEN",
      "ASCITL",   "ASSIST A CITIZEN",
      "ASCITM",   "ASSIST A CITIZEN",
      "ASDSSL",   "ASSIST DSS",
      "ASFDL",    "ASSIST FIRE DEPARTMENT",
      "ASFDM",    "ASSIST FIRE DEPARTMENT",
      "ASLEF",    "ASSIST LAW ENFORCEMENT",
      "ASLEM",    "ASSIST LAW ENFORCEMENT",
      "ASVCF",    "ASSAULT VICTIM",
      "ASVCFA",   "ASSAULT VICTIM",
      "ASVCFB",   "ASSAULT VICTIM",
      "ASVCFC",   "ASSAULT VICTIM",
      "ASVCFD",   "ASSAULT VICTIM",
      "ASVCFE",   "ASSAULT VICTIM",
      "ASVCL",    "ASSAULT VICTIM",
      "ASVCM",    "ASSAULT VICTIM",
      "ASVCMA",   "ASSAULT VICTIM",
      "ASVCMB",   "ASSAULT VICTIM",
      "ASVCMC",   "ASSAULT VICTIM",
      "ASVCMD",   "ASSAULT VICTIM",
      "ASVCME",   "ASSAULT VICTIM",
      "AT",       "AUTO THEFT",
      "ATL",      "ATTEMPT TO LOCATE",
      "BACKF",    "BACK PAINS/PROBLEMS",
      "BACKFA",   "BACK PAINS/PROBLEMS",
      "BACKFB",   "BACK PAINS/PROBLEMS",
      "BACKFC",   "BACK PAINS/PROBLEMS",
      "BACKFD",   "BACK PAINS/PROBLEMS",
      "BACKFE",   "BACK PAINS/PROBLEMS",
      "BACKM",    "BACK PAINS/PROBLEMS",
      "BACKMA",   "BACK PAINS/PROBLEMS",
      "BACKMB",   "BACK PAINS/PROBLEMS",
      "BACKMC",   "BACK PAINS/PROBLEMS",
      "BACKMD",   "BACK PAINS/PROBLEMS",
      "BACKME",   "BACK PAINS/PROBLEMS",
      "BACKUP",   "BACKUP",
      "BARRIC",   "BARRICADED SUBJECT",
      "BE",       "BURGLARY / BREAKING AND ENTERING",
      "BOATF",    "BOATING ACCIDENT",
      "BOATL",    "BOAT RELATED CALLS",
      "BOATM",    "BOATING ACCIDENT",
      "BOLO",     "BE ON LOOK OUT",
      "BOMBF",    "BOMB THREAT FIRE",
      "BOMBL",    "BOMB THREAT LAW",
      "BOMBM",    "BOMB THREAT MEDICAL",
      "BRNCOM",   "BURN COMPLAINT",
      "BRNCOML",  "BURN COMPLAINT",
      "BRUSH",    "BRUSH, TREES OR WOODS FIRE",
      "BSN",      "BUSINESS FIRE",
      "BSNL",     "BUSINESS STRUCTURE FIRE",
      "BSNM",     "COMMERCIAL STRUCTURE FIRE",
      "BURNF",    "PERSON WITH BURNS",
      "BURNFA",   "PERSON WITH BURNS",
      "BURNFB",   "PERSON WITH BURNS",
      "BURNFC",   "PERSON WITH BURNS",
      "BURNFD",   "PERSON WITH BURNS",
      "BURNFE",   "PERSON WITH BURNS",
      "BURNM",    "PERSON WITH BURNS",
      "BURNMA",   "PERSON WITH BURNS",
      "BURNMB",   "PERSON WITH BURNS",
      "BURNMC",   "PERSON WITH BURNS",
      "BURNMD",   "PERSON WITH BURNS",
      "BURNME",   "PERSON WITH BURNS",
      "CARJAC",   "CAR-JACKING",
      "CAVEF",    "CAVE IN FIRE",
      "CAVEL",    "CAVE IN LAW",
      "CAVEM",    "CAVE IN MEDICAL",
      "CHASE",    "CHASE",
      "CHESF",    "CHEST PAINS",
      "CHESFA",   "CHEST PAINS",
      "CHESFB",   "CHEST PAINS",
      "CHESFC",   "CHEST PAINS",
      "CHESFD",   "CHEST PAINS",
      "CHESFE",   "CHEST PAINS",
      "CHESM",    "CHEST PAINS",
      "CHESMA",   "CHEST PAINS",
      "CHESMB",   "CHEST PAINS",
      "CHESMC",   "CHEST PAINS",
      "CHESMD",   "CHEST PAINS",
      "CHESME",   "CHEST PAINS",
      "CHOKF",    "PERSON CHOKING",
      "CHOKFA",   "PERSON CHOKING",
      "CHOKFB",   "PERSON CHOKING",
      "CHOKFC",   "PERSON CHOKING",
      "CHOKFD",   "PERSON CHOKING",
      "CHOKFE",   "PERSON CHOKING",
      "CHOKM",    "PERSON CHOKING",
      "CHOKMA",   "PERSON CHOKING",
      "CHOKMB",   "PERSON CHOKING",
      "CHOKMC",   "PERSON CHOKING",
      "CHOKMD",   "PERSON CHOKING",
      "CHOKME",   "PERSON CHOKING",
      "CKAREA",   "CHECK THE AREA",
      "CKBSN",    "CHECK A BUSINESS",
      "CKFOUT",   "CHECK IF FIRE OUT",
      "CKRES",    "CHECK A RESIDENCE",
      "CKSHOT",   "CHECK SHOTS IN THE AREA",
      "CMDET",    "CARBON MONOXIDE DETECTOR",
      "CMF",      "CARBON MONOXIDE POISONING",
      "CMFA",     "CARBON MONOXIDE POISONING",
      "CMFB",     "CARBON MONOXIDE POISONING",
      "CMFC",     "CARBON MONOXIDE POISONING",
      "CMFD",     "CARBON MONOXIDE POISONING",
      "CMFE",     "CARBON MONOXIDE POISONING",
      "CMM",      "CARBON MONOXIDE POISONING",
      "CMMA",     "CARBON MONOXIDE POISONING",
      "CMMB",     "CARBON MONOXIDE POISONING",
      "CMMC",     "CARBON MONOXIDE POISONING",
      "CMMD",     "CARBON MONOXIDE POISONING",
      "CMME",     "CARBON MONOXIDE POISONING",
      "CONT",     "COUNTERFEIT",
      "CONTRL",   "CONTROLLED BURN",
      "CORONER",  "CORONER  RESPONSE / NOTIFY",
      "COURT",    "OUT FOR COURT",
      "DDIS",     "DOMESTIC DISTURBANCE",
      "DETHF",    "OBVIOUS DEATH FIRE",
      "DETHFA",   "OBVIOUS DEATH FIRE",
      "DETHFB",   "OBVIOUS DEATH FIRE",
      "DETHFC",   "OBVIOUS DEATH FIRE",
      "DETHFD",   "OBVIOUS DEATH FIRE",
      "DETHFE",   "OBVIOUS DEATH FIRE",
      "DETHL",    "OBVIOUS DEATH",
      "DETHM",    "OBIVOUS DEATH MEDICAL",
      "DETHMA",   "OBIVOUS DEATH MEDICAL",
      "DETHMB",   "OBIVOUS DEATH MEDICAL",
      "DETHMC",   "OBIVOUS DEATH MEDICAL",
      "DETHMD",   "OBIVOUS DEATH MEDICAL",
      "DETHME",   "OBIVOUS DEATH MEDICAL",
      "DIABF",    "DIABETIC COMPLICATIONS",
      "DIABFA",   "DIABETIC COMPLICATIONS",
      "DIABFB",   "DIABETIC COMPLICATIONS",
      "DIABFC",   "DIABETIC COMPLICATIONS",
      "DIABFD",   "DIABETIC COMPLICATIONS",
      "DIABFE",   "DIABETIC COMPLICATIONS",
      "DIABM",    "DIABETIC COMPLICATIONS",
      "DIABMA",   "DIABETIC COMPLICATIONS",
      "DIABMB",   "DIABETIC COMPLICATIONS",
      "DIABMC",   "DIABETIC COMPLICATIONS",
      "DIABMD",   "DIABETIC COMPLICATIONS",
      "DIABME",   "DIABETIC COMPLICATIONS",
      "DIFBF",    "DIFFICULTY BREATHING",
      "DIFBFA",   "DIFFICULTY BREATHING",
      "DIFBFB",   "DIFFICULTY BREATHING",
      "DIFBFC",   "DIFFICULTY BREATHING",
      "DIFBFD",   "DIFFICULTY BREATHING",
      "DIFBFE",   "DIFFICULTY BREATHING",
      "DIFBM",    "DIFFICULTY BREATHING",
      "DIFBMA",   "DIFFICULTY BREATHING",
      "DIFBMB",   "DIFFICULTY BREATHING",
      "DIFBMC",   "DIFFICULTY BREATHING",
      "DIFBMD",   "DIFFICULTY BREATHING",
      "DIFBME",   "DIFFICULTY BREATHING",
      "DIS",      "DISTURBANCE",
      "DISPC",    "DISTURBING THE PEACE",
      "DISWP",    "DISTURBANCE WITH WEAPONS",
      "DRUGS",    "DRUG RELATED",
      "DRVBY",    "DRIVEBY SHOOTING",
      "DRWNF",    "DROWNING VICTIM",
      "DRWNFA",   "DROWNING VICTIM",
      "DRWNFB",   "DROWNING VICTIM",
      "DRWNFC",   "DROWNING VICTIM",
      "DRWNFD",   "DROWNING VICTIM",
      "DRWNFE",   "DROWNING VICTIM",
      "DRWNL",    "DROWNING     POOL / BATHTUB",
      "DRWNM",    "DROWNING VICTIM",
      "DRWNMA",   "DROWNING VICTIM",
      "DRWNMB",   "DROWNING VICTIM",
      "DRWNMC",   "DROWNING VICTIM",
      "DRWNMD",   "DROWNING VICTIM",
      "DRWNME",   "DROWNING VICTIM",
      "DTRANM",   "DEATH TRANSPORT",
      "DUI",      "DRIVING UNDER INFLUENCE",
      "DV",       "DOMESTIC VIOLENCE",
      "DVICEF",   "DEVICE FOUND FIRE",
      "DVICEL",   "DEVICE FOUND LAW",
      "DVICEM",   "DEVICE FOUND MEDICAL",
      "DVWP",     "DOMESTIC VIOLENCE WITH A WEAPON",
      "ELECF",    "ELECTROCUTION",
      "ELECFA",   "ELECTROCUTION",
      "ELECFB",   "ELECTROCUTION",
      "ELECFC",   "ELECTROCUTION",
      "ELECFD",   "ELECTROCUTION",
      "ELECFE",   "ELECTROCUTION",
      "ELECM",    "ELECTROCUTION",
      "ELECMA",   "ELECTROCUTION",
      "ELECMB",   "ELECTROCUTION",
      "ELECMC",   "ELECTROCUTION",
      "ELECMD",   "ELECTROCUTION",
      "ELECME",   "ELECTROCUTION",
      "ELVTP",    "PERSON(S) TRAPPED IN ELEVATOR",
      "ELVTPM",   "PERSON(S) TRAPPED IN ELEVATOR MEDICAL",
      "EOC",      "EOC ACTIVATION",
      "ESCAPE",   "ESCAPED CONVICT",
      "ESCRTC",   "ESCORT CITIZEN",
      "ESCRTP",   "ESCORT PRISONER",
      "EXPLOF",   "CHECK FOR AN EXPLOSION",
      "EXPLOL",   "EXPLOSION LAW",
      "EXPOSF",   "EXPOSURE HEAT/COLD",
      "EXPOSFA",  "EXPOSURE HEAT/COLD",
      "EXPOSFB",  "EXPOSURE HEAT/COLD",
      "EXPOSFC",  "EXPOSURE HEAT/COLD",
      "EXPOSFD",  "EXPOSURE HEAT/COLD",
      "EXPOSFE",  "EXPOSURE HEAT/COLD",
      "EXPOSM",   "EXPOSURE  HEAT / COLD",
      "EXPOSMA",  "EXPOSURE  HEAT / COLD",
      "EXPOSMB",  "EXPOSURE  HEAT / COLD",
      "EXPOSMC",  "EXPOSURE  HEAT / COLD",
      "EXPOSMD",  "EXPOSURE  HEAT / COLD",
      "EXPOSME",  "EXPOSURE  HEAT / COLD",
      "EYEF",     "EYE PROBLEMS",
      "EYEFA",    "EYE PROBLEMS",
      "EYEFB",    "EYE PROBLEMS",
      "EYEFC",    "EYE PROBLEMS",
      "EYEFD",    "EYE PROBLEMS",
      "EYEFE",    "EYE PROBLEMS",
      "EYEM",     "EYE PROBLEMS",
      "EYEMA",    "EYE PROBLEMS",
      "EYEMB",    "EYE PROBLEMS",
      "EYEMC",    "EYE PROBLEMS",
      "EYEMD",    "EYE PROBLEMS",
      "EYEME",    "EYE PROBLEMS",
      "FALLF",    "FALL",
      "FALLFA",   "FALL",
      "FALLFB",   "FALL",
      "FALLFC",   "FALL",
      "FALLFD",   "FALL",
      "FALLFE",   "FALL",
      "FALLM",    "FALL",
      "FALLMA",   "FALL",
      "FALLMB",   "FALL",
      "FALLMC",   "FALL",
      "FALLMD",   "FALL",
      "FALLME",   "FALL",
      "FALSE",    "FALSE CALL",
      "FGT",      "FIGHT",
      "FGTWP",    "FIGHT WITH WEAPONS",
      "FIGHT",    "FIGHT RELATED",
      "FIREWK",   "FIREWORKS COMPLAINT",
      "FLAG",     "FLAG DOWN",
      "FORG",     "FORGERY",
      "FOUND",    "PERSON FOUND",
      "FRAUD",    "FRAUD RELATED",
      "FUP",      "FOLLOW UP",
      "GAS",      "GAS ODOR IN/OUT",
      "GRASS",    "GRASS FIRE",
      "HANGUP",   "911 HANGUP",
      "HAZMATF",  "HAZARDOUS MATERIAL SPILL, LEAK OR FIRE",
      "HAZMATL",  "HAZMAT RELATED",
      "HAZMATM",  "HAZARDOUS MATERIAL SPILL, LEAK OR FIRE",
      "HEARTF",   "HEART PROBLEMS",
      "HEARTFA",  "HEART PROBLEMS",
      "HEARTFB",  "HEART PROBLEMS",
      "HEARTFC",  "HEART PROBLEMS",
      "HEARTFD",  "HEART PROBLEMS",
      "HEARTFE",  "HEART PROBLEMS",
      "HEARTM",   "HEART PROBLEMS",
      "HEARTMA",  "HEART PROBLEMS",
      "HEARTMB",  "HEART PROBLEMS",
      "HEARTMC",  "HEART PROBLEMS",
      "HEARTMD",  "HEART PROBLEMS",
      "HEARTME",  "HEART PROBLEMS",
      "HEMOF",    "HEMORAGING",
      "HEMOFA",   "HEMORAGING",
      "HEMOFB",   "HEMORAGING",
      "HEMOFC",   "HEMORAGING",
      "HEMOFD",   "HEMORAGING",
      "HEMOFE",   "HEMORAGING",
      "HEMOM",    "HEMORAGING",
      "HEMOMA",   "HEMORAGING",
      "HEMOMB",   "HEMORAGING",
      "HEMOMC",   "HEMORAGING",
      "HEMOMD",   "HEMORAGING",
      "HEMOME",   "HEMORAGING",
      "HIT",      "HIT 10 MIN OR 1 HR",
      "HOME",     "HOME INVASION",
      "INDEXP",   "INDECENT EXPOSURE",
      "INTOX",    "INTOXICATED SUBJECT",
      "KEEP",     "KEEP CHECK",
      "KIDNAP",   "KIDNAPPING",
      "LACF",     "LACERATION",
      "LACFA",    "LACERATION",
      "LACFB",    "LACERATION",
      "LACFC",    "LACERATION",
      "LACFD",    "LACERATION",
      "LACFE",    "LACERATION",
      "LACM",     "LACERATION",
      "LACMA",    "LACERATION",
      "LACMB",    "LACERATION",
      "LACMC",    "LACERATION",
      "LACMD",    "LACERATION",
      "LACME",    "LACERATION",
      "LARC",     "LARCENY",
      "LITTER",   "LITTER CONTROL",
      "LNS",      "LINES DOWN OR ARCING",
      "LNSARC",   "POWER LINE ARCING FIRE CALL",
      "LNSDN",    "POWER LINE DOWN",
      "LOCKF",    "CHILD LOCKED IN VECHICLE",
      "LOCKL",    "LOCKED OUT",
      "LOCKM",    "CHILD LOCKED IN VECHICLE",
      "LOSTF",    "LOST PERSON",
      "LOSTL",    "LOST PERSON",
      "LOSTM",    "LOST PERSON",
      "LZ",       "SET UP LANDING ZONE",
      "MAYDAY",   "MAYDAY FIREMAN DOWN",
      "MISSINGF", "MISSING PERSON",
      "MISSINGL", "MISSING PERSON",
      "MISSINGM", "MISSING PERSON",
      "MNDNF",    "CHECK A MAN DOWN",
      "MNDNFA",   "CHECK A MAN DOWN",
      "MNDNFB",   "CHECK A MAN DOWN",
      "MNDNFC",   "CHECK A MAN DOWN",
      "MNDNFD",   "CHECK A MAN DOWN",
      "MNDNFE",   "CHECK A MAN DOWN",
      "MNDNL",    "MAN DOWN",
      "MNDNM",    "CHECK A MAN DOWN",
      "MNDNMA",   "CHECK A MAN DOWN",
      "MNDNMB",   "CHECK A MAN DOWN",
      "MNDNMC",   "CHECK A MAN DOWN",
      "MNDNMD",   "CHECK A MAN DOWN",
      "MNDNME",   "CHECK A MAN DOWN",
      "MSCSRF",   "MISCELLANEOUS SERVICE FOR FIRE",
      "MSCSRL",   "MISCELLANEOUS SERVICE FOR LAW",
      "MSCSRM",   "MISCELLANEOUS SERVICE",
      "MSG",      "MESSAGE FOR AGENCY",
      "MSGF",     "MESSAGE FIRE",
      "MSGM",     "MESSAGE EMS",
      "MTRANM",   "MEDICAL TRANSPORT",
      "MURDER",   "MURDER",
      "NOTBRF",   "SUBJECT NOT BREATHING",
      "NOTBRM",   "SUBJECT NOT BREATHING",
      "NOTIFY",   "NOTIFY AGENCY",
      "OBF",      "OBSTETRICAL EMERGENCY",
      "OBFA",     "OBSTETRICAL EMERGENCY",
      "OBFB",     "OBSTETRICAL EMERGENCY",
      "OBFC",     "OBSTETRICAL EMERGENCY",
      "OBFD",     "OBSTETRICAL EMERGENCY",
      "OBFE",     "OBSTETRICAL EMERGENCY",
      "OBM",      "OBSTETRICAL EMERGENCY",
      "OBMA",     "OBSTETRICAL EMERGENCY",
      "OBMB",     "OBSTETRICAL EMERGENCY",
      "OBMC",     "OBSTETRICAL EMERGENCY",
      "OBMD",     "OBSTETRICAL EMERGENCY",
      "OBME",     "OBSTETRICAL EMERGENCY",
      "ODF",      "OVERDOSE",
      "ODFA",     "OVERDOSE",
      "ODFB",     "OVERDOSE",
      "ODFC",     "OVERDOSE",
      "ODFD",     "OVERDOSE",
      "ODFE",     "OVERDOSE",
      "ODM",      "OVERDOSE",
      "ODMA",     "OVERDOSE",
      "ODMB",     "OVERDOSE",
      "ODMC",     "OVERDOSE",
      "ODMD",     "OVERDOSE",
      "ODME",     "OVERDOSE",
      "OFCDN",    "OFFICER DOWN",
      "OFCDNF",   "OFFICER DOWN",
      "OS",       "UNIT OUT OF SERVICE",
      "OUT",      "OUTSIDE STRUCTURE FIRE",
      "OUTM",     "OUTSIDE BUILDING FIRE",
      "POISF",    "POISONING",
      "POISFA",   "POISONING",
      "POISFB",   "POISONING",
      "POISFC",   "POISONING",
      "POISFD",   "POISONING",
      "POISFE",   "POISONING",
      "POISM",    "POISONING",
      "POISMA",   "POISONING",
      "POISMB",   "POISONING",
      "POISMC",   "POISONING",
      "POISMD",   "POISONING",
      "POISME",   "POISONING",
      "POLCOM",   "POLICE COMPLAINT",
      "POLE",     "UTILITY POLE FIRE",
      "PSYCHF",   "PSYCHOLOGICAL PROBLEM",
      "PSYCHFA",  "PSYCHOLOGICAL PROBLEM",
      "PSYCHFB",  "PSYCHOLOGICAL PROBLEM",
      "PSYCHFC",  "PSYCHOLOGICAL PROBLEM",
      "PSYCHFD",  "PSYCHOLOGICAL PROBLEM",
      "PSYCHFE",  "PSYCHOLOGICAL PROBLEM",
      "PSYCHL",   "PSYCHOLOGICAL PROBLEMS",
      "PSYCHM",   "PSYCHOLOGICAL PROBLEMS",
      "PSYCHMA",  "PSYCHOLOGICAL PROBLEMS",
      "PSYCHMB",  "PSYCHOLOGICAL PROBLEMS",
      "PSYCHMC",  "PSYCHOLOGICAL PROBLEMS",
      "PSYCHMD",  "PSYCHOLOGICAL PROBLEMS",
      "PSYCHME",  "PSYCHOLOGICAL PROBLEMS",
      "PUNCF",    "PUNCTURE WOUND",
      "PUNCFA",   "PUNCTURE WOUND",
      "PUNCFB",   "PUNCTURE WOUND",
      "PUNCFC",   "PUNCTURE WOUND",
      "PUNCFD",   "PUNCTURE WOUND",
      "PUNCFE",   "PUNCTURE WOUND",
      "PUNCM",    "PUNCTURE WOUND",
      "PUNCMA",   "PUNCTURE WOUND",
      "PUNCMB",   "PUNCTURE WOUND",
      "PUNCMC",   "PUNCTURE WOUND",
      "PUNCMD",   "PUNCTURE WOUND",
      "PUNCME",   "PUNCTURE WOUND",
      "RADIOF",   "RADIO PROBLEMS FIRE",
      "RADIOL",   "RADIO PROBLEMS LAW",
      "RADIOM",   "RADIO PROBLEMS EMS",
      "RECOV",    "RECOVERED PROPERTY",
      "REPO",     "REPOSESSION",
      "RES",      "RESIDENTIAL STRUCTURE FIRE",
      "RESL",     "RESIDENTIAL STRUCTURE FIRE",
      "RESM",     "RESIDENTIAL STRUCTURE FIRE",
      "ROB",      "ROBBERY",
      "RUN",      "RUNAWAY",
      "SA",       "SUSPISCIOUS AUTO",
      "SEIZF",    "SEIZURE",
      "SEIZFA",   "SEIZURE",
      "SEIZFB",   "SEIZURE",
      "SEIZFC",   "SEIZURE",
      "SEIZFD",   "SEIZURE",
      "SEIZFE",   "SEIZURE",
      "SEIZM",    "SEIZURE",
      "SEIZMA",   "SEIZURE",
      "SEIZMB",   "SEIZURE",
      "SEIZMC",   "SEIZURE",
      "SEIZMD",   "SEIZURE",
      "SEIZME",   "SEIZURE",
      "SEXA",     "SEXUAL ASSAULT",
      "SEXAF",    "SEXUAL ASSAULT",
      "SEXAFA",   "SEXUAL ASSAULT",
      "SEXAFB",   "SEXUAL ASSAULT",
      "SEXAFC",   "SEXUAL ASSAULT",
      "SEXAFD",   "SEXUAL ASSAULT",
      "SEXAFE",   "SEXUAL ASSAULT",
      "SEXAL",    "SEXUAL ASSULT",
      "SEXAM",    "SEXUAL ASSAULT",
      "SEXAMA",   "SEXUAL ASSAULT",
      "SEXAMB",   "SEXUAL ASSAULT",
      "SEXAMC",   "SEXUAL ASSAULT",
      "SEXAMD",   "SEXUAL ASSAULT",
      "SEXAME",   "SEXUAL ASSAULT",
      "SEXOFN",   "SEXUAL OFFENSE",
      "SHOPIC",   "SHOPLIFTER IN CUSTODY",
      "SHOTF",    "SHOOTING",
      "SHOTFA",   "SHOOTING",
      "SHOTFB",   "SHOOTING",
      "SHOTFC",   "SHOOTING",
      "SHOTFD",   "SHOOTING",
      "SHOTFE",   "SHOOTING",
      "SHOTL",    "SHOTS FIRED",
      "SHOTM",    "SHOOTING",
      "SHOTMA",   "SHOOTING",
      "SHOTMB",   "SHOOTING",
      "SHOTMC",   "SHOOTING",
      "SHOTMD",   "SHOOTING",
      "SHOTME",   "SHOOTING",
      "SICKF",    "SICK PERSON",
      "SICKFA",   "SICK PERSON",
      "SICKFB",   "SICK PERSON",
      "SICKFC",   "SICK PERSON",
      "SICKFD",   "SICK PERSON",
      "SICKFE",   "SICK PERSON",
      "SICKM",    "SICK PERSON",
      "SICKMA",   "SICK PERSON",
      "SICKMB",   "SICK PERSON",
      "SICKMC",   "SICK PERSON",
      "SICKMD",   "SICK PERSON",
      "SICKME",   "SICK PERSON",
      "SMK",      "SMOKE IN AREA",
      "SMKDET",   "CHECK SMOKE DETECTOR",
      "SP",       "SUSPICIOUS PERSON",
      "SPASF",    "SPECIAL ASSIGNMENT FIRE",
      "SPASL",    "SPECIAL ASSIGNMENT LAW",
      "SPASM",    "SPECIAL ASSIGNMENT EMS",
      "SPILL",    "FUEL SPILL",
      "SPILLL",   "FUEL SPILL",
      "SPPKG",    "SUSPICOUS PACKAGE",
      "SPWP",     "SUSPICIOUS PERSON WITH WEAPONS",
      "SRO",      "SCHOOL RESOURCE OFFICER",
      "STABF",    "STABBING/CUTTING",
      "STABFA",   "STABBING/CUTTING",
      "STABFB",   "STABBING/CUTTING",
      "STABFC",   "STABBING/CUTTING",
      "STABFD",   "STABBING/CUTTING",
      "STABFE",   "STABBING/CUTTING",
      "STABL",    "STABBING  / CUTTING",
      "STABM",    "STABBING/CUTTING",
      "STABMA",   "STABBING/CUTTING",
      "STABMB",   "STABBING/CUTTING",
      "STABMC",   "STABBING/CUTTING",
      "STABMD",   "STABBING/CUTTING",
      "STABME",   "STABBING/CUTTING",
      "STEMIM",   ">> STEMI <<",
      "STLVEH",   "STALLED VEHICLE",
      "STNDBY",   "STAND BY TO AVOID A DISTURBANCE",
      "STRKF",    "STROKE",
      "STRKFA",   "STROKE",
      "STRKFB",   "STROKE",
      "STRKFC",   "STROKE",
      "STRKFD",   "STROKE",
      "STRKFE",   "STROKE",
      "STRKM",    "STROKE",
      "STRKMA",   "STROKE",
      "STRKMB",   "STROKE",
      "STRKMC",   "STROKE",
      "STRKMD",   "STROKE",
      "STRKME",   "STROKE",
      "SUBF",     "SUBMERSION",
      "SUBL",     "SUBMERSSION LAW",
      "SUBM",     "SUBMERSION",
      "SUICF",    "SUICIDE",
      "SUICFA",   "SUICIDE",
      "SUICFB",   "SUICIDE",
      "SUICFC",   "SUICIDE",
      "SUICFD",   "SUICIDE",
      "SUICFE",   "SUICIDE",
      "SUICL",    "SUICIDE",
      "SUICM",    "SUICIDE",
      "SUICMA",   "SUICIDE",
      "SUICMB",   "SUICIDE",
      "SUICMC",   "SUICIDE",
      "SUICMD",   "SUICIDE",
      "SUICME",   "SUICIDE",
      "TESTF",    "FIRE TEST CALL",
      "TESTL",    "LAW TEST CALL",
      "TESTM",    "EMS TEST CALL",
      "THREAT",   "THREAT REPORT",
      "TOW",      "TOW RELATED",
      "TOWER",    "CHECK TOWER LIGHTS",
      "TRAFFIC",  "TRAFFIC RELATED",
      "TRAIN",    "TRAIN ACCIDENT, DERAILMENT OR FIRE",
      "TRAINL",   "TRAIN INCIDENT",
      "TRAINM",   "TRAIN ACCIDENT, DERAILMENT OR FIRE",
      "TRASH",    "TRASH FIRE",
      "TRAUMAF",  "TRAUMATIC INJURY",
      "TRAUMAFA", "TRAUMATIC INJURY",
      "TRAUMAFB", "TRAUMATIC INJURY",
      "TRAUMAFC", "TRAUMATIC INJURY",
      "TRAUMAFD", "TRAUMATIC INJURY",
      "TRAUMAFE", "TRAUMATIC INJURY",
      "TRAUMAM",  "TRAUMATIC INJURY",
      "TRAUMAMA", "TRAUMATIC INJURY",
      "TRAUMAMB", "TRAUMATIC INJURY",
      "TRAUMAMC", "TRAUMATIC INJURY",
      "TRAUMAMD", "TRAUMATIC INJURY",
      "TRAUMAME", "TRAUMATIC INJURY",
      "TREE",     "TREE DOWN",
      "TRES",     "TRESPASSING",
      "TRUANT",   "TRUANT",
      "TSTOP",    "TRAFFIC STOP",
      "UNAMF",    "UNKNOWN CALL FOR AMBULANCE",
      "UNAMFA",   "UNKNOWN CALL FOR AMBULANCE",
      "UNAMFB",   "UNKNOWN CALL FOR AMBULANCE",
      "UNAMFC",   "UNKNOWN CALL FOR AMBULANCE",
      "UNAMFD",   "UNKNOWN CALL FOR AMBULANCE",
      "UNAMFE",   "UNKNOWN CALL FOR AMBULANCE",
      "UNAMM",    "UNKNOWN CALL FOR AMBULANCE",
      "UNAMMA",   "UNKNOWN CALL FOR AMBULANCE",
      "UNAMMB",   "UNKNOWN CALL FOR AMBULANCE",
      "UNAMMC",   "UNKNOWN CALL FOR AMBULANCE",
      "UNAMMD",   "UNKNOWN CALL FOR AMBULANCE",
      "UNAMME",   "UNKNOWN CALL FOR AMBULANCE",
      "UNCOF",    "SUBJECT UNCONSCIOUS",
      "UNCOFA",   "SUBJECT UNCONSCIOUS",
      "UNCOFB",   "SUBJECT UNCONSCIOUS",
      "UNCOFC",   "SUBJECT UNCONSCIOUS",
      "UNCOFD",   "SUBJECT UNCONSCIOUS",
      "UNCOFE",   "SUBJECT UNCONSCIOUS",
      "UNCOM",    "SUBJECT UNCONSCIOUS",
      "UNCOMA",   "SUBJECT UNCONSCIOUS",
      "UNCOMB",   "SUBJECT UNCONSCIOUS",
      "UNCOMC",   "SUBJECT UNCONSCIOUS",
      "UNCOMD",   "SUBJECT UNCONSCIOUS",
      "UNCOME",   "SUBJECT UNCONSCIOUS",
      "UNFD",     "UNKNOWN CALL FOR FIRE DEPT",
      "UNLE",     "UNKNOWN LAW CALL",
      "UWOC",     "USE WITH OUT OWNERS CONSENT",
      "VAND",     "VANDALISM",
      "VEH",      "VEHICLE FIRE",
      "VEHM",     "VEHICLE ON FIRE/PERSONS TRAPPED",
      "VICE",     "VICE RELATED",
      "WAR",      "WARRANTS",
      "WARDO",    "WARRANT FOR DETENTION ORDER",
      "WASH",     "WASH DOWN",
      "WELF",     "CHECK THE WELFARE OF A CITIZEN",
      "WELFA",    "CHECK THE WELFARE OF A CITIZEN",
      "WELFB",    "CHECK THE WELFARE OF A CITIZEN",
      "WELFC",    "CHECK THE WELFARE OF A CITIZEN",
      "WELFD",    "CHECK THE WELFARE OF A CITIZEN",
      "WELFE",    "CHECK THE WELFARE OF A CITIZEN",
      "WELL",     "WELFARE CHECK",
      "WELM",     "CHECK THE WELFARE OF A CITIZEN",
      "WELMA",    "CHECK THE WELFARE OF A CITIZEN",
      "WELMB",    "CHECK THE WELFARE OF A CITIZEN",
      "WELMC",    "CHECK THE WELFARE OF A CITIZEN",
      "WELMD",    "CHECK THE WELFARE OF A CITIZEN",
      "WELME",    "CHECK THE WELFARE OF A CITIZEN",
      "XCATE",    "EXTRICATION RESPONSE",
      "XCATEL",   "EXTRICATION - LAW",
      "XCATEM",   "EXTRICATION - EMS"
  });
}
