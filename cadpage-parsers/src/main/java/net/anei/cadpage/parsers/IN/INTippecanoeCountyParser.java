package net.anei.cadpage.parsers.IN;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INTippecanoeCountyParser extends DispatchSPKParser {

  public INTippecanoeCountyParser() {
    super("TIPPECANOE COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "caliber_alert@tippecanoe.in.gov";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    data.strCode = data.strCall;
    data.strCall = convertCodes(data.strCode, CALL_CODES);
    int pt = data.strCity.indexOf('|');
    if (pt >= 0) data.strCity = data.strCity.substring(0,pt).trim();
    return true;
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "911UNK",       "911 UNKNOWN",
      "AA",           "ADULT ABUSE",
      "ABNBIK",       "ABANDONED BICYCLE",
      "ABNVEH",       "ABANDONED VEHICLE",
      "AIRCFT",       "AIRCRAFT INCIDENT",
      "ALARM",        "ALARM HOLD UP/BURGLAR/PANIC",
      "ALCVIO",       "ALCOHOL VIOLATION",
      "ALRMTE",       "ALARM TEST",
      "AMBTRA",       "AMBULANCE TRANSFER",
      "ANIMAL",       "ANIMAL COMPLAINT",
      "ARG",          "ARGUMENT",
      "ARSON",        "ARSON INVEST",
      "AS",           "AIRPORT STANDBY",
      "ASOTD",        "ASSIST OTHER DEPARTMENTS",
      "ASTAMB",       "ASSIST AMBULANCE",
      "ASTCPS",       "ASSIST CPS",
      "ASTFIR",       "ASSIST FIRE DEPARTMENT",
      "AT RES",       "ATTEMPTED RESIDENTIAL ENTRY",
      "ATHEFT",       "ATTEMPTED THEFT",
      "ATTBE",        "ATTEMPTED BURGLARY",
      "ATTROB",       "ATTEMPTED ROBBERY",
      "AUTOMI",       "AUTO DAMAGE NOT ACCIDENT",
      "AUTTFT",       "AUTO THEFT",
      "BATTER",       "BATTERY",
      "BICVIO",       "BICYCLE VIOLATIONS",
      "BLDLOC",       "BUILDING LOCK OUT",
      "BOLO",         "BOLO",
      "BOMBDE",       "BOMB DETAIL",
      "BOMBTH",       "BOMB THREAT",
      "BURG",         "BURGLARY",
      "BURSAR",       "BURSAR DETAIL",
      "CHLDAB",       "CHILD ABUSE",
      "CHLDMO",       "CHILD MOLEST",
      "CIVIL",        "CIVIL MATTER",
      "CKWB",         "CHECK WELL BEING",
      "CP",           "COMMUNITY POLICING",
      "CRASH FATAL",  "FATAL CRASH",
      "CRASH H&R",    "CRASH HIT & RUN",
      "CRASH PD",     "CRASH PD",
      "CRASH PI",     "CRASH WITH INJURY",
      "CRIMEP",       "CRIME PREVENTION",
      "CRMMIS",       "CRIMINAL MISCHIEF",
      "CSA",          "CSA REPORT",
      "DAMAG",        "DAMAGED PROPERTY",
      "DEATH",        "DEATH",
      "DELMSG",       "DELIVER MESSAGE",
      "DIGPRO",       "DIGNITARY PROTECTION",
      "DISORD",       "DISORDERLY CONDUCT",
      "DISTRB",       "DISTURBANCE",
      "DISVEH",       "DISABLED VEHICLE",
      "DIVE",         "DIVE DETAIL",
      "DOMSTC",       "DOMESTIC",
      "DPINFO",       "DEPT INFO",
      "DRNKDR",       "DRUNK DRIVER",
      "DRONE",        "DRONE DETAIL",
      "DROWN",        "WATER/ICE RESCUE (DROWNING)",
      "DRUG",         "DRUG LAW VIOLATION",
      "DUMP",         "DUMPING",
      "EDOTR",        "EDO/MENTAL TRANSPORT",
      "EMC",          "EMERGENCY CUSTODY ORDER",
      "ESCAPE",       "ESCAPE OR JAIL BREAK",
      "ESCORT",       "ESCORT",
      "ETS",          "ETS ALARM (PURDUE POLICE)",
      "EVICT",        "EVICTION",
      "EXPAT",        "EXPATROL",
      "EXPLOS",       "EXPLOSION/BOMBING",
      "FALSE",        "FALSE INFORMING",
      "FDTEST",       "FIRE TEST  weekly schedule test",
      "FIGHT",        "FIGHT",
      "FIR",          "FIRE CALL MISC",
      "FIRALM",       "FIRE ALARM",
      "FIRBLD",       "FIRE BUILDING/ STRUCTURE",
      "FIRCSR",       "FIRE CONFINED SPACE RESCUE",
      "FIRDRI",       "FIRE DRILL",
      "FIRENT",       "FIRE ELEVATOR RESCUE",
      "FIREWK",       "FIREWORKS",
      "FIRFLD",       "GRASS, BRUSH OR FIELD FIRE OFF ROAD.",
      "FIRGAS",       "GAS LEAK",
      "FIRGRA",       "FIRE GRASS/TRASH/MULCH",
      "FIRHAZ",       "FIRE HAZMAT/ SPILLS",
      "FIRINS",       "FIRE/LIFE SAFETY INSPECTION",
      "FIRLK",        "FIRE WATER LEAK",
      "FIRMED",       "FIRE MEDICAL RESPONSE",
      "FIRODO",       "FIRE ODOR",
      "FIRSAF",       "FIRE SAFETY WALK THROUGH",
      "FIRSVC",       "FIRE CO/SMOKE DETECTOR",
      "FIRTBL",       "FIRE/SYSTEM TROUBLE",
      "FIRTNK",       "TANKER/SEMI VEHICLE FIRE",
      "FIRTRN",       "FIRE DEPT TRANSPORT",
      "FIRVEH",       "VEHICLE FIRE",
      "FLGDWN",       "FLAG DOWN",
      "FOLLUP",       "FOLLOW UP",
      "FOOTPT",       "FOOT PURSUIT",
      "FOPROP",       "FOUND PROPERTY",
      "FORGE",        "FORGERY",
      "FOUNDC",       "FOUND CHILD",
      "FRAUD",        "FRAUD/SCAM/COUNTERFEIT",
      "GRAFFI",       "GRAFFITI",
      "HARASS",       "HARASSMENT",
      "HARPHO",       "HARASSMENT (PHONE/INTERNET)",
      "HOUSCH",       "HOUSE CHECK",
      "HUNTWI",       "HUNTING/WILDLIFE",
      "IDTHEF",       "IDENTITY THEFT",
      "INDEXP",       "INDECENT EXPOSURE",
      "INFO",         "INFORMATION",
      "INJDOC",       "INJURY DOCUMENTATION",
      "INTIMI",       "INTIMIDATION",
      "JINC",         "JAIL INCIDENT",
      "JUVPRO",       "JUVENILE PROBLEM",
      "K9",           "K9DETAIL",
      "KIDNAP",       "KIDNAPPING/ABDUCTION",
      "LOOKIN",       "LOOKING FOR SUBJECT",
      "LOSTPR",       "LOST PROPERTY",
      "LPDFUG",       "LPD FUGITIVE ON WLFI",
      "MAIT",         "MAINTENANCE",
      "MANGUN",       "MAN WITH A GUN",
      "MANKNV",       "MAN WITH KNIFE",
      "MEDPRO",       "MEDICAL PROBLEM",
      "MEDVAI",       "MEDICS NOT AVAILABLE",
      "MENTAL",       "MENTAL PROBLEM",
      "MINCON",       "MINOR  CONSUMING",
      "MISC",         "MISCELLANEOUS",
      "MISEND",       "MISSING/ENDANGERED CHILD",
      "MISPLS",       "MISSING PLS CLIENT",
      "MISSNG",       "MISSING PERSON",
      "MURDER",       "MURDER",
      "NO TOW",       "NO TOW",
      "NOELEV",       "NO RESPONSE ELEVATOR",
      "NOISE",        "NOISE COMPLAINTS",
      "NONMED",       "NON-MEDICAL RUNS",
      "OFFDTY",       "OFF DUTY ASSIST",
      "OPENCO",       "OPEN CONTAINER",
      "OPENDR",       "OPEN DOOR",
      "OPNBRN",       "OPEN BURN",
      "ORD",          "ORDINANCE VIOLATIONS",
      "OVERDS",       "OVERDOSE",
      "PAPER",        "PAPERWORK DETAILS/COURTWORK",
      "PARKIN",       "PARKING PROBLEM",
      "PARTY",        "PARTY",
      "PERMIT",       "PERMIT PARKING VIOLATION",
      "POLY",         "POLYGRAPH EXAMINATION",
      "PPVIO",        "PROTECT PURDUE VIOLATION",
      "PRISON",       "PRISONER DETAIL",
      "PROSTN",       "PROSTITUTION/TRAFFICKING",
      "PROTEC",       "PO/RESTRAINING ORDER VIOLATION",
      "PROWLR",       "PROWLER",
      "PUBINT",       "PUBLIC INTOX",
      "RAPE",         "RAPE",
      "RECAUTO",      "RECOVERED AUTO",
      "RECDRV",       "RECKLESS DRIVER/TRAFFIC",
      "REGCHK",       "SEX OFFENDER REGISTRY CHECK",
      "REGVIO",       "REGISTRY VIOLATION",
      "REPO",         "REPO",
      "RESIDE",       "RESIDENTIAL ENTRY",
      "RESIST",       "RESISTING LAW ENFORCEMENT",
      "ROADCL",       "ROAD CLOSING",
      "ROADRA",       "ROAD RAGE",
      "ROBBRY",       "ROBBERY",
      "RUNAWA",       "RUNAWAY RETURNED",
      "RUNAWY",       "RUNAWAY",
      "SCHGRD",       "SCHOOL GUARD DUTY",
      "SCHWK",        "SCHOOL WALK THROUGH",
      "SEARCH",       "SEARCH WARRANT",
      "SEC",          "SECURITY DETAIL",
      "SEXOFF",       "SEX OFFENSE",
      "SHOOT",        "SHOOTING",
      "SHOTS",        "SHOTS FIRED/ HEARD SOMEWHERE",
      "SICK",         "EMPLOYEE SICK",
      "SKATE",        "SKATEBOARDING COMPLAINTS/CITATIONS",
      "SLIDEO",       "VEHICLE SLIDE OFF",
      "SOLIC",        "SOLICITING / PANHANDLING",
      "SPKSUB",       "SPEAK TO SUBJECT",
      "STAB",         "STABBING",
      "STALK",        "STALKING",
      "STAND",        "STAND BY DETAIL",
      "SUICID",       "SUICIDE/ SUICIDAL SUBJECT",
      "SUSINC",       "SUSPICIOUS INCIDENT",
      "SUSPCK",       "SUSPICIOUS PACKAGE",
      "SUSPER",       "SUSPICIOUS PERSON",
      "SUSVEH",       "SUSPICIOUS VEHICLE",
      "SWAT",         "SWAT CALL OUT",
      "TEST",         "TEST CALL",
      "TFTBIK",       "THEFT OF A BIKE",
      "TFTFTP",       "THEFT/FAILURE TO PAY",
      "TFTPRO",       "THEFT OF PROPERTY",
      "TFTSHP",       "THEFT/ BY SHOPLIFTING",
      "TFTVEH",       "THEFT FROM VEHICLE",
      "THREAT",       "THREATS",
      "TOWPVT",       "TOW/PRIVATE PROPERTY",
      "TP",           "PURSUIT/TRAFFIC",
      "TRAFCV",       "TRAFFIC VIOLATION",
      "TRAINI",       "TRAINING",
      "TRANSP",       "TRANSPORT",
      "TRESPA",       "TRESPASSING",
      "TRF24H",       "TRAFFIC 24 HR TAG",
      "TRF4HR",       "TRAFFIC 4 HOUR",
      "TRF72H",       "TRAFFIC 72 HOUR COMPLAINT",
      "TRFBLK",       "BLOCKING DRIVE COMPLAINT",
      "TRFCON",       "TRAFFIC CONTROL",
      "TRFHAZ",       "TRAFFIC HAZARD OBJECT IN ROAD",
      "TRFPPR",       "TRAFFIC PRIVATE PROPERTY",
      "TRFREP",       "TRAFFIC REPAIR/SIGNALS/LIGHTS",
      "TRFRPS",       "TRAFFIC PARKING REQUEST",
      "TRFTOW",       "TRAFFIC TOW/IMPOUND",
      "TRFWHL",       "WHEEL LOCK",
      "TRUA",         "TRUANCY",
      "TS",           "TRAFFIC STOP",
      "TSSRCH",       "TRAFFIC STOP W/VEH SEARCH",
      "UNATP",        "UNATTENDED PACKAGE",
      "UNAUTH",       "UNAUTHORIZED CONTROL",
      "UNKPRO",       "UNKNOWN PROBLEM",
      "UNWANT",       "UNWANTED GUEST",
      "VEHAST",       "VEHICLE/MOTORIST ASSIST",
      "VEHLOC",       "VEHICLE LOCK OUT",
      "VEHREL",       "VEHICLE RELEASE",
      "VIN CH",       "VIN CHECK",
      "WARRANT",      "NEW WARRANT ENTRY",
      "WARSVC",       "WARRANT SERVICE",
      "WEAPON",       "WEAPONS VIOLATION",
      "WEATHR",       "WEATHER RELATED CALLS",
      "WL20-1",       "WL EXECUTIVE ORDER 20-1",
      "WLORDC",       "WLORDINANCE CONTAINER",
      "WLORDD",       "WLORDINANCE SIGNAGE",
      "WLORDF",       "WLORDINANCE FURNITURE",
      "WLORDG",       "WLORDINANCE GRASS/WEEDS",
      "WLORDP",       "WLORDINANCE PARKING",
      "WLORDR",       "WLORDINANCE RENTAL VIOLATION",
      "WLORDS",       "WLORDINANCE SNOW/ICE",
      "WLORDT",       "WLORDINANCE TRASH",
      "WORKRV",       "WORK RELEASE VIOLATION",
      "WOW",          "WOW LOOKING FOR SUBJ."

  });

}
