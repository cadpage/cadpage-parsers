package net.anei.cadpage.parsers.WV;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WVGreenbrierCountyParser extends SmartAddressParser {
  
  public WVGreenbrierCountyParser() {
    super("GREENBRIER COUNTY", "WV");
    setFieldList("DATE TIME CODE CALL ADDR APT CITY ST INFO");
  }
  
  @Override
  public String getFilter() {
    return "greenbrier@pagingpts.com";
  }
  
  private static final Pattern MASTER = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d(?: [AP]M)?) ([A-Z0-9/]+) (\\d+ .*)");
  private static DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");
  private static final Pattern INFO_PFX_PTN = Pattern.compile("[A-Z, ]+\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d: *");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("(?:The|This) message was sent .*|\" on .*");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    String info = null;
    int pt = body.indexOf("\n\n");
    if (pt >= 0) {
      info = body.substring(pt+2).trim();
      body = body.substring(0, pt).trim();
    }
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strDate = match.group(1);
    String time = match.group(2);
    if (time.endsWith("M")) {
      setTime(TIME_FMT, time, data);
    } else {
      data.strTime = time;
    }
    
    data.strCode = match.group(3);
    data.strCall = convertCodes(data.strCode, CALL_CODES);
    
    String addr = match.group(4).trim();
    Parser p = new Parser(addr);
    String city = p.getLastOptional(',');
    if (city.equals("WV")) {
      data.strState = city;
      city = p.getLastOptional(',');
    }
    data.strCity = city;
    addr = stripFieldStart(p.get(), "0 ");
    parseAddress(addr, data);
    
    if (info != null) {
      for (String line : info.split("\n")) {
        line = line.trim();
        match = INFO_PFX_PTN.matcher(line);
        if (match.lookingAt()) line = line.substring(match.end());
        if (INFO_JUNK_PTN.matcher(line).matches()) continue;
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }

    return true;
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "911ABN", "911 ABANDONED/OPEN",
      "ABDMN",  "ABDOMINAL PAIN",
      "ABDUC",  "ABDUCTION/KIDNAP",
      "ABNVH",  "ABANDONED VEHICLE",
      "ABUSE",  "ABUSE/CHILD ELDER",
      "AIRCF",  "AIRCRAFT INCIDENT",
      "ALLGY",  "ALLERGIC REACTION",
      "ALRBG",  "ALARM BURGLAR",
      "ALRFR",  "ALARM FIRE",
      "ALRHU",  "ALARM HOLD UP",
      "ALRM",   "ALARM/GENERAL/UNK",
      "ALRMD",  "ALARM MEDICAL",
      "ALRPN",  "ALARM PANIC",
      "AMBER",  "AMBER ALERT",
      "AMPTN",  "AMPUTATION",
      "ANMCN",  "ANIMAL CONTROL",
      "APS",    "ADULT PROTECTIVE SER",
      "ARREST", "ARREST",
      "ASLT",   "ASSAULT",
      "ASTHM",  "ASTHMA",
      "B/E",    "BREAKING/ENTERING",
      "BACK",   "BACK PAIN/INJURY",
      "BATT",   "BATTERY",
      "BITE",   "BITE",
      "BLEED",  "BLEED",
      "BOLO",   "BE ON LOOKOUT",
      "BOMB",   "BOMB/THREAT",
      "BP",     "BLOOD PRESSURE",
      "BRDIF",  "BREATHING DIFFICULTY",
      "BURG",   "BURGLARY",
      "BURN",   "BURN/MEDICAL",
      "CABLE",  "CABLE PROBLEM",
      "CBMNX",  "CARBON MONOXIDE",
      "CHBRT",  "CHILD BIRTH",
      "CHF",    "CONGESTIVE HEART FAI",
      "CHOKE",  "CHOKING",
      "CHSPN",  "CHEST PAIN",
      "CI",     "CRIMINAL INVESTIGATI",
      "CIRC",   "CIRCULATION",
      "CIVIL",  "CIVIL MATTER",
      "CLOT",   "BLOOD CLOT",
      "CPRAD",  "CPR ADULT",
      "CPRCH",  "CPR CHILD",
      "CPRTC",  "CPR TRACH",
      "CPS",    "CHILD PROTECTIVE SER",
      "CRDC",   "CARDIAC",
      "CUSDS",  "CUSTODY DISPUTE",
      "DHYDR",  "DEHYDRATION",
      "DIABT",  "DIABETIC",
      "DIZZY",  "DIZZINESS",
      "DMSTC",  "DOMESTIC",
      "DNRLW",  "DNR LAW",
      "DNRWL",  "DNR WILDLIFE",
      "DOA",    "DEAD ON ARRIVAL",
      "DOG",    "DOG COMPLAINT",
      "DRGAC",  "DRUG ACTIVITY",
      "DROWN",  "DROWN/WATER INJ",
      "DRVWO",  "DRIVING WITHOUT PERM",
      "DSBVH",  "DISABLED VEHICLE",
      "DSLC",   "DISLOCATION",
      "DSTB",   "DISTURBANCE",
      "DSTPRP", "DESTRUCTION PROPERTY",
      "DUI",    "DUI",
      "DVPRQ",  "DVP REQUEST",
      "DVPVL",  "DVP VIOLATION",
      "ELCUT",  "ELECTROCUTION",
      "ENVIR",  "ENVIRONMENTAL",
      "ESCPE",  "ESCAPEE",
      "ESCRT",  "ESCORT",
      "EVAC",   "EVACUATION",
      "EVAL",   "EVALUATION",
      "EXPSUR", "EXPOSURE",
      "EXTRC",  "EXTRICATION",
      "EYE",    "EYE INJURY",
      "FALL",   "FALL",
      "FIGHT",  "FIGHT",
      "FIRARSN","FIRE ARSON",
      "FIRBRU", "FIRE BRUSH",
      "FIRELCT","FIRE ELECTRICAL",
      "FIRFLUE","FIRE FLUE",
      "FIRRKND","FIRE REKINDLE",
      "FIRSTRCT","FIRE STRUCTURE",
      "FIRUNK", "FIRE/UNKNOWN",
      "FIRVHCL","FIRE VEHICLE",
      "FLOOD",  "FLOODING",
      "FLWUP",  "FOLLOW UP",
      "FOBJCT", "FOREIGN OBJECT",
      "FOUND",  "FOUND",
      "FRAUD",  "FRAUD",
      "FRBRK",  "FRACTURE BROKEN",
      "FRGRY",  "FORGERY",
      "FVR",    "FEVER",
      "GAS",    "GAS PROBLEM/LEAK",
      "GNRTR",  "GENERATOR PROBLEM",
      "GSDRV",  "GAS DRIVE OFF",
      "GSW",    "GUNSHOT WOUND",
      "HEAD",   "HEADPAIN/INJURY",
      "HMCDE",  "HOMICIDE",
      "HMCON",  "HOME CONFINEMENT",
      "HRASS",  "HARASSMENT",
      "HSTAG",  "HOSTAGE",
      "HTRN",   "HIT AND RUN",
      "HUMAN",  "HUMAN RESOURCES",
      "HZCND",  "HAZARD RD CONDITIONS",
      "HZMAT",  "HAZMAT",
      "ILBRN",  "ILLEGAL BURNING",
      "ILDRV",  "ILLEGAL DRIVER",
      "ILL",    "ILLNESS GENERAL",
      "INFCT",  "INFECTION",
      "INTOX",  "PUBLIC INTOX",
      "JUVNL",  "JUVENILE PROBLEM",
      "LAC",    "LACERATION",
      "LARC",   "LARCENY",
      "LETH",   "LETHARGY",
      "LFAST",  "LIFT ASSIST",
      "LOST",   "LOST",
      "LPR",    "LPR HIT",
      "LTTR",   "LITTERING",
      "LVSTCK", "LIVESTOCK",
      "LZ",     "LANDING ZONE SET UP",
      "MAG",    "MAGISTRATE",
      "MDAST",  "MEDIC ASSIST",
      "MEDEX",  "MEDICAL EXAMINER",
      "MINE",   "MINE ACCIDENT/INCIDE",
      "MNTHG",  "MENTAL HYGIENE",
      "MNTL",   "MENTAL STATUS",
      "MOCK",   "MOCK INC/DRILL",
      "MSSNG",  "MISSING PERSON",
      "MUTAID", "MUTUAL AID",
      "MVA",    "MVA",
      "NAUSEA", "NAUSEA",
      "NCIC",   "NCIC HIT",
      "NUMB",   "NUMBNESS",
      "OBGYN",  "OBGYN",
      "OBSTC",  "OBSTRUCTING",
      "OD",     "OVERDOSE",
      "OFAST",  "OFFICER ASSIST",
      "OI",     "OFFICER INITIATED",
      "PAIN",   "PAIN",
      "PAPSE",  "PAPER SERVICE",
      "PHONE",  "PHONE PROBLEM",
      "PNEUM",  "PNEUMONIA",
      "POISN",  "POISON",
      "POSS",   "POSSESSION",
      "POWER",  "POWER PROBLEM",
      "PRBVIO", "PROBATION VIOLATION",
      "PRSUT",  "PURSUIT",
      "PRWL",   "PROWLER",
      "PS",     "PUBLIC SERVICE",
      "PTRL",   "PATROL REQUEST",
      "RABIES", "RABID ANIMAL",
      "RADIO",  "RADIO PROBLEMS",
      "RAIL",   "RAILROAD INCIDENT",
      "RASH",   "RASH/HIVES",
      "RBBRY",  "ROBBERY",
      "RCKLS",  "RECKLESS DRIVER",
      "RDOBS",  "ROADWAY OBSTRUCTION",
      "RECOV",  "RECOVERY",
      "REPO",   "REPOSSESSION",
      "RESCUE", "RESCUE",
      "RNWY",   "RUNAWAY",
      "SHPLF",  "SHOPLIFTER",
      "SHTFR",  "SHOT FIRED",
      "SLVR",   "SILVER ALERT",
      "SMOKE",  "SMOKE",
      "SOLIC",  "SOLICITING",
      "SOS",    "SOS",
      "SRVC",   "SERVICE CALL",
      "STAB",   "STAB WOUND",
      "STDBY",  "STANDBY",
      "STRKE",  "STROKE",
      "SUCAT",  "SUICIDE ATTEMPT",
      "SUICD",  "SUICIDE",
      "SUSPC",  "SUSPICIOUS",
      "SWELL",  "SWELLING",
      "SXASL",  "SEXUAL ASSAULT",
      "SYNCP",  "SYNCOPE/FAINTING",
      "SZR",    "SEIZURE",
      "TCHRES", "TECHNICAL RESCUE",
      "TEST",   "TEST",
      "THEFT",  "THEFT",
      "THRT",   "THREATS",
      "TOW",    "TOW REQUEST",
      "TOWER",  "TOWER PROBLEMS",
      "TRAIN",  "TRAIN ACCIDENT/INCID",
      "TRAUM",  "TRAUMA",
      "TRFCN",  "TRAFFIC CONTROL",
      "TRSPS",  "TRESPASS",
      "TX",     "TRANSPORT",
      "UNCNS",  "UNCONCIOUS",
      "UNK",    "UNKNOWN",
      "UNRSP",  "UNRESPONSIVE",
      "UOF",    "USE OF FORCE",
      "URINE",  "URINARY",
      "VNDL",   "VANDALISM",
      "VOMIT",  "VOMIT",
      "VRBL",   "VERBAL DISPUTE",
      "WATER",  "WATER PROBLEM",
      "WEAK",   "WEAKNESS",
      "WLBNG",  "WELL BEING CHECK",
      "WNTD",   "WANTED SUBJECT",
      "WRNT",   "WARRANT SERVE/CHECK"
  });
}
