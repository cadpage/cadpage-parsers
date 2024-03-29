package net.anei.cadpage.parsers.GA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;

public class GAWashingtonCountyBParser extends DispatchA78Parser {

  public GAWashingtonCountyBParser() {
    super(CALL_CODES, "WASHINGTON COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "CAD@ssialerts.com,donotreply@WashingtonCountyE911alerts.com";
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[] {
      "911HU",        "911 HANGUP",
      "ABDUCT",       "ABDUCTION/KIDNAP",
      "AIRCRAFT",     "AIRCRAFT DOWN",
      "ALARM1",       "BREAKIN/BURGLAR ALARM",
      "ALARMB",       "BANK ALARM",
      "ALARMH",       "HOLD-UP/PANIC ALARM",
      "ANIMAL",       "ANIMAL COMPLAINT",
      "ARMROB",       "ARMED ROBBERY",
      "ASSAULT",      "ASSAULT",
      "ASSIST",       "ASSIST AGENCY",
      "ASSTMOT",      "ASSIST MOTORIST",
      "BOMB",         "BOMB THREAT/ATTEMPT",
      "BURGLARY",     "BURGLARY REPORT",
      "CARCASS",      "CARCASS",
      "CHEMREL",      "CHEMICAL RELEASE",
      "CHILD",        "CHILD CUSTODY",
      "CHILDAB",      "CHILD ABUSE",
      "CHILDM",       "CHILD MOLESTATION",
      "CIVILP",       "CIVIL PAPER",
      "COUNTER",      "COUNTERFEIT",
      "COURT",        "COURT",
      "CROWD",        "CROWD",
      "DAMAGE",       "PROPERTY DAMAGE",
      "DEBRIS",       "ROADWAY DEBRIS",
      "DIRECT",       "DIRECT TRAFFIC",
      "DISORDERLY",   "DISORDERLY SUBJECT",
      "DISTURBANCE",  "DISTURBANCE",
      "DOMESTIC",     "DOMESTIC REPORT",
      "DOMPROG",      "DOMESTIC IN PROGRESS",
      "DRAGRACING",   "DRAG RACING",
      "DRUGS",        "DRUG INVESTIGATION",
      "DRUGSUBJ",     "SUBJECT WITH DRUGS",
      "DUMP",         "DUMPING TRASH",
      "EMSABD",       "ABDOMINAL PAIN",
      "EMSAIR",       "AIRWAY OBSTRUCTION",
      "EMSALL",       "ALLERGIC REACTION",
      "EMSALM",       "MEDICAL ALARM",
      "EMSANM",       "ANIMAL BITE",
      "EMSBLD",       "BLEEDING",
      "EMSBPN",       "BACK PAIN",
      "EMSBRN",       "BURN",
      "EMSBRP",       "BREATHING PROBLEMS",
      "EMSCAR",       "CARDIAC ARREST",
      "EMSCDB",       "CHILD BIRTH",
      "EMSCHO",       "CHOKING",
      "EMSCHP",       "CHEST PAIN",
      "EMSCMI",       "CARBON MONOXIDE",
      "EMSCON",       "CONVULSIONS/SEIZURES",
      "EMSCOVID",     "EMS COVID 19",
      "EMSDIA",       "DIABETIC",
      "EMSDRO",       "DROWNING",
      "EMSEBS",       "EBOLA SYMPTOMS",
      "EMSELE",       "ELECTROCUTION",
      "EMSEXP",       "HEAT/COLD EXPOSURE",
      "EMSEYE",       "EYE INJURY",
      "EMSFAI",       "FAINT",
      "EMSFAL",       "FALL",
      "EMSGUN",       "GUNSHOT WOUND",
      "EMSHEA",       "HEADACHE",
      "EMSHRT",       "HEART",
      "EMSIND",       "INDUSTRIAL ACCIDENT",
      "EMSMUT",       "MUTUAL AID",
      "EMSOTH",       "EMS OTHER",
      "EMSOVD",       "OVERDOSE",
      "EMSPSY",       "PSYCHIATRIC",
      "EMSSIC",       "SICK PERSON",
      "EMSSTA",       "STABBING",
      "EMSSTR",       "STROKE",
      "EMSUNC",       "UNCONSCIOUS",
      "EMSUNK",       "UNKNOWN MEDICAL",
      "ESCORT",       "ESCORT",
      "FIGHT",        "FIGHT",
      "FIREA",        "FIRE ALARM",
      "FIREC",        "COMMERCIAL STRUCTURE",
      "FIRECB",       "CONTROL BURN",
      "FIRED",        "DUMPSTER FIRE",
      "FIREE",        "TRANSFORMERS/WIRES",
      "FIREG",        "GRASS FIRE",
      "FIREILL",      "ILLEGAL BURNING",
      "FIREINV",      "FIRE INVESTIGATION",
      "FIRELZ",       "MEDIVAC STANDBY",
      "FIRERES",      "RESIDENTIAL STRUCTURE",
      "FIRES",        "STRUCTURE FIRE",
      "FIRETR",       "TRASH FIRE",
      "FIREV",        "VEHICLE FIRE",
      "FIREW",        "WOODS FIRE",
      "FIREWO",       "FIREWORKS",
      "FLAGDO",       "FLAG DOWN",
      "FOLLOW",       "FOLLOW UP",
      "FORGERY",      "FORGERY",
      "FOUND",        "FOUND PROPERTY",
      "FRAUD",        "FRAUD",
      "GASDOF",       "GAS DRIVE OFF",
      "GASLK",        "GAS LEAK",
      "GUNSHOT",      "SUBJ SHOT",
      "HARASS",       "HARASSMENT",
      "HELP",         "OFFICER ASSISTANCE",
      "ILLPARK",      "ILLEGALLY PARKED VEH",
      "INSPECT",      "VEH/TRAILER INSPECTION",
      "INTOXD",       "INTOXICATED DRIVER",
      "INTOXP",       "INTOXICATED PERSON",
      "JUVENILE",     "JUVENILE COMPLAINT",
      "LIVEST",       "LIVESTOCK",
      "LOCK",         "LOCK OUT",
      "LOST",         "LOST PROPERTY",
      "MAKECO",       "MAKE CONTACT",
      "MENTAL",       "MENTAL SUBJECT",
      "MISC",         "MISC CALL",
      "MISSPER",      "MISSING/LOST PERSON",
      "MURDER",       "MURDER",
      "MVA",          "MOTOR VEHICLE ACCIDENT",
      "MVAENT",       "MVA WITH ENTRAPMENT",
      "MVAINJ",       "MVA WITH INJURIES",
      "MVARUN",       "MVA HIT & RUN",
      "MVAU",         "MVA UNKNOWN INJURIES",
      "MVAUNK",       "MVA UNKNOWN INJURIES",
      "NOISE",        "NOISE COMPLAINT",
      "ODOR",         "ODOR OTHER THAN SMOKE",
      "OPEN",         "OPEN DOOR/WINDOW",
      "PED AUTO",     "PEDESTRIAN VS. AUTO",
      "PROWLER",      "PROWLER REPORTED",
      "PURSUIT",      "FOOT/VEHICLE PURSUIT",
      "RECKLESS",     "RECKLESS DRIVER",
      "RESCOLL",      "RESCUE COLLAPSE",
      "RESCON",       "RESCUE CONFINED SPACE",
      "RESCUE",       "VICTIM RESCUE",
      "RESHA",        "RESCUE HIGH ANGLE",
      "RIOT",         "RIOT",
      "ROAD",         "ROAD HAZARD",
      "ROADBLK",      "ROAD BLOCK",
      "ROADREP",      "ROAD REPAIR",
      "ROBBERY",      "ROBBERY",
      "RUNAWAY",      "RUNAWAY PERSON",
      "SCHTRA",       "SCHOOL TRAFFIC",
      "SECCHK",       "SECURITY CHECK",
      "SEXOFF",       "SEX OFFENSE",
      "SHOPLIFT",     "SHOPLIFTING",
      "SHOTS",        "SHOTS FIRED",
      "SPDAUTO",      "SPEEDING AUTO",
      "SPILL",        "CHEMICAL SPILL",
      "STALK",        "STALKING",
      "STOVEH",       "STOLEN VEHICLE",
      "SUBJDO",       "SUBJECT DOWN",
      "SUBJGUN",      "SUBJECT WITH GUN",
      "SUBPO",        "SUBPOENA",
      "SUICIDE",      "SUICIDE ATTEMPT",
      "SUSPACT",      "SUSPICIOUS ACTIVITY",
      "SUSPPER",      "SUSPICIOUS PERSON",
      "SUSPVEH",      "SUSPICIOUS VEHICLE",
      "TEST",         "TEST CALL",
      "THEFT",        "THEFT REPORT",
      "THEFTEA",      "THEFT BY ENTERING AUTO",
      "THREAT",       "TERRORISTIC THREATS",
      "TRAFFIC",      "TRAFFIC STOP",
      "TRAFLGT",      "TRAFFIC LIGHT PROBLEM",
      "TRAIN",        "TRAIN DERAILMENT",
      "TRANS",        "PRISON TRANSPORT",
      "TRANSFER",     "EMS TRANSFER",
      "TREE",         "TREE DOWN",
      "TRESP",        "TRESPASSING",
      "UNWANT",       "UNWANTED SUBJECT",
      "VANDAL",       "VANDALISM",
      "VEHABA",       "ABANDONED VEHICLE",
      "VEHDIS",       "DISABLED VEHICLE",
      "VEHSTO",       "STOLEN VEHICLE",
      "WARRANT",      "WARRANT SERVICE",
      "WEADEB",       "DEBRIS IN ROADWAY/WEATHER",
      "WEAFLR",       "ROAD FLOODING/WEATHER",
      "WEAFLS",       "FLOODING IN STRUCTURE/WEATHER",
      "WEAHAIL",      "HAIL REPORTED",
      "WEAPON",       "POSSESSION OF WEAPON",
      "WEATR1",       "TREE DOWN IN ROAD/WEATHER",
      "WEATR2",       "TREE DOWN ON STRUCTURE/WEATHER",
      "WEAWIR",       "WIRES DOWN/WEATHER",
      "WELFARE",      "WELFARE CHECK",
      "WIRES",        "WIRES DOWN"

  });
}
