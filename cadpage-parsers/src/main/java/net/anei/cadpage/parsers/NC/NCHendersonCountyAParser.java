package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NCHendersonCountyAParser extends SmartAddressParser {
  
  private static final Pattern ID_PTN = Pattern.compile("^(?:HCSO ?PageGate ?Service:)?(\\d{8}) +");
  private static final Pattern LINE_PTN = Pattern.compile(" Line\\d+=");
  private static final Pattern UNIT_PTN = Pattern.compile(" +([-#,A-Z0-9#]+)$", Pattern.CASE_INSENSITIVE);
  private static final Pattern PHONE_PTN = Pattern.compile(" +(?:(\\d{3}-\\d{3}-\\d{4}(?: *[xX]\\d+)?)|\\d{3}- -)$");
  
  public NCHendersonCountyAParser() {
    super(CITY_LIST, "HENDERSON COUNTY", "NC");
    setFieldList("ID ADDR APT CITY CODE CALL NAME PHONE UNIT INFO");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    // Strip call id from front of text
    Matcher  match = ID_PTN.matcher(body);
    if (!match.find()) return false;
    data.strCallId = match.group(1);
    body = body.substring(match.end());
    
    // and remove the Linenn= terms from end of message
    match = LINE_PTN.matcher(body);
    if (match.find()) {
      int firstPt = match.start();
      int lastPt = match.end();
      while (match.find()) {
        String info = body.substring(lastPt, match.start()).trim();
        lastPt = match.end();
        data.strSupp = append(data.strSupp, " / ", info);
      }
      String info = body.substring(lastPt).trim();
      data.strSupp = append(data.strSupp, " / ", info);
      body = body.substring(0,firstPt).trim();
    }
    
    // Look for and remove UNIT term
    match = UNIT_PTN.matcher(body);
    if (!match.find()) return false;
    data.strUnit = match.group(1).toUpperCase();
    body = body.substring(0, match.start());
    
    // Look for and remove PHONE
    match = PHONE_PTN.matcher(body);
    if (match.find()) {
      data.strPhone = getOptGroup(match.group(1));
      body = body.substring(0, match.start());
    }
    
    // Parse the address and city
    // We have to fudge the BLKD RD call because it makes the
    // city look like part of a new road name
    body = body.replace("//", "/").replace("BLKD RD", "BLKD-RD").replace("PWR LN", "PWR-LN").replace("LIFE LN", "LIFE-LN");
    
    // THere is *ALWAYS* a city name
    // When we do not find one, the usual reason is a malformed address
    Result res1 = parseAddress(StartType.START_ADDR, FLAG_PAD_FIELD_EXCL_CITY, body);
    if (res1.getCity().length() == 0) {
      Result res2 = parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY, body);
      if (res2.getCity().length() > 0) {
        res1 = null;
        res2.getData(data);
        parseAddress(res2.getStart(), data);
        body = res2.getLeft();
      }
    }
    if (res1 != null) {
      res1.getData(data);
      data.strApt = append(data.strApt, "-", res1.getPadField());
      body = res1.getLeft();
    }
    
    body = body.replace("BLKD-RD", "BLKD RD").replace("PWR-LN", "PWR LN").replace("LIFE-LN", "LIFE LN");
    
    // Split what is left into call description and name
    CodeTable.Result res = CALL_TABLE.getResult(body);
    if (res != null) {
      data.strCall = res.getDescription();
      data.strName = cleanWirelessCarrier(res.getRemainder());
      String code = res.getCode();
      char chr = code.charAt(0);
      if (chr == 'C' || Character.isDigit(chr)) {
        int pt = code.indexOf(' ');
        if (pt >= 0) code = code.substring(0,pt);
        data.strCode = code;
      }
    } else {
      data.strCall = body;
    }
    
    return true;
  }
  
  private static final CodeTable CALL_TABLE = new CodeTable(
      "100 RAPE",      "RAPE OR SEXUAL ASSAULT",
      "101 CHLD ABU",  "CHILD ABUSE",
      "102 VANDALIS",  "VANDALISM",
      "103 LARCENY",   "LARCENY",
      "103F FRAUD",    "FRAUD",
      "103G LARC GA",  "GAS DRIVE OFF",
      "103I ID THEF",  "ID THEFT",
      "103V VEH LAR",  "LARCENY OF VEHICLE",
      "104 WARRANT",   "WARRANT",
      "105 COURT",     "COURT",
      "106 HEA/WELF",  "HEALTH WELFARE CHECK",
      "107 COMM REL",  "COMMUNITY RELATIONS",
      "107S SCHL CK",  "SCHOOL CHECK",
      "108 CRIM PRV",  "CRIME PREVENT",
      "109 SUICIDE",   "SUICIDE",
      "110 OUT CNTY",  "OUTSIDE COUNTY",
      "111 EX SECUR",  "EXTRA SECURITY",
      "111B BUS SEC",  "SCHOOL BUS SECURITY",
      "111H HSING",    "HOUSING SECURITY CHECK",
      "111S SUB SEC",  "SUBDIVISION SECURITY",
      "111T TRF ENF",  "TRAFFIC ENFORCEMENT",
      "112 ASSAULT",   "ASSAULT",
      "113 SUS VEH",   "SUSP VEH STOP",
      "114 FOOT PAT",  "FOOT PATROL",
      "118 INC 911",   "INCOMPLETE 911 CALL",
      "118W INCW911",  "INCOMPLETE WIRELESS 911",
      "119 CVL PAPR",  "SERV CIVIL PAPER",
      "119D DOM VIO",  "DOM VIOL SERVICE",
      "12 STAND BY",   "STAND BY",
      "120 MURDER",    "MURDER",
      "121 SEX OFF",   "SEX OFFENDER INVESTIGATION",
      "123 SPC DUTY",  "SPECIAL DUTY",
      "124 OUT AREA",  "OUT OF ASSIGNED AREA",
      "125 CRIM SAT",  "CRIME AREA SATURATION",
      "13 WEATHER",    "WEATHER",
      "130 TRAINING",  "TRAINING",
      "14 EMRG MESS",  "EMERGENCY MESSAGE",
      "145 K9 SCH S",  "K9 SCHOOL SEARCH",
      "21 PH COMP",    "TELEPHONE COMP",
      "25 MEET WITH",  "MEET IN PERSON",
      "27 LIC CHECK",  "LICENSE CHECK",
      "31 PICK UP",    "PICK UP",
      "33 OFFCR DWN",  "OFFICER DOWN",
      "35 LIGHT OUT",  "TRAFFIC LIGHT OUT",
      "37 BLCK RDWY",  "BLOCKED ROADWAY",
      "38 RADAR ACT",  "RADAR ACTIVITY",
      "39 ABDN VEH",   "ABANDON VEHICLE",
      "40 FIGHT",      "FIGHT N PROG",
      "43 CHASE",      "CHASE",
      "44 RIOT",       "RIOT",
      "45 BOMB THRT",  "BOMB THREAT",
      "46 BNK ALARM",  "BANK ALARM",
      "49 DRAG RCNG",  "DRAG RACING",
      "53 RD BLOCK",   "RD BLOCK",
      "54 HIT & RUN",  "HIT & RUN",
      "55 DWI",        "DWI",
      "56 INTX PED",   "INTOX PED",
      "57 BRTHALYZR",  "BREATHALYZER",
      "58 DIR TRAF",   "DIRECT TRAFFIC",
      "59 R ESCORT",   "ROUTINE TRANSPORT",
      "59B BLOOD RU",  "BLOOD RUN",
      "59F FNRL TRP",  "FUNERAL TRANSPORT",
      "6 BUSY",        "BUSY",
      "60A SUSP ACT",  "SUSPICIOUS ACTIVITY",
      "60D OPEN DOR",  "OPEN DOOR OR WINDOW",
      "60P SUSP PER",  "SUSPICIOUS PERSON",
      "60T TRESPASS",  "TRESPASS",
      "60V SUSP VEH",  "SUSPICIOUS VEHICLE",
      "62 B&E&L",      "B&E&L",
      "62B B&E BSN",   "B&E BUSINESS",
      "62R B&E RES",   "B&E RESIDENCE",
      "62V B&E VEH",   "B&E VEHICLE",
      "63 OTHR INV",   "OTHER INVESTIGATION",
      "63A SEX ABUS",  "SEXUAL ABUSE INVESTIGATION",
      "63B BIOHAZAR",  "BIO HAZARD",
      "63S SUP RPRT",  "SUPPLIMENTAL REPORT",
      "65 ARMD ROB",   "ARMED ROBBERY",
      "66 MED EXAM",   "NOTIFY MED EXAMINER",
      "67 DEATH INV",  "DEATH INVESTIGATION",
      "68 LVSTK PRB",  "LIVESTOCK PROBLEM",
      "70 IMP PRKED",  "IMPROPERLY PARKED VEHICLE",
      "72 SUB CUST",   "SUBJ IN CUSTODY",
      "72C PRIS OC",   "PRISONER TO OTHER COUNTY",
      "72D PRIS DR",   "PRISONER TO DOCTOR",
      "72T PRIS TRP",  "PRISONER TRANSPORT",
      "73 MNTL SUBJ",  "MENTAL SUBJECT",
      "73P MNTL PPR",  "MENTAL PAPER",
      "73T MNTL TRP",  "MENTAL TRIP",
      "74 ESCAPE",     "ESCAPE",
      "75 WANTED",     "STOLEN OR WANTED",
      "76 PROWLER",    "PROWLER",
      "77 AST O/AGY",  "ASSIST OTHER AGENCY",
      "77D ASST DSS",  "ASSIST DSS",
      "78 UNIT HELP",  "78 UNIT NEEDS HELP",
      "79 DISTURBNC",  "DISTURBANCE",
      "79D BRKG DOG",  "BARKING DOG",
      "79N NOISE DI",  "79N NOISE DISTURBANCE",
      "80 DOMESTIC",   "DOMESTIC",
      "80A DOM ASST",  "DOMESTIC ASSIST",
      "81 RET DEPT",   "RETURN TO DEPT",
      "82 SUBJ WEAP",  "SUBJ W/WEAPON",
      "82D DISCHARG",  "GUN DISCHARGE",
      "85 STRND VEH",  "STRANDED VEH",
      "85K KEYS VEH",  "KEYS IN VEH",
      "87A AMBER AL",  "AMBER ALERT INFO",
      "87M MISS PER",  "MISSING PERSON",
      "87R RUN JUV",   "RUNAWAY JUVENILE",
      "87S SLVR ALT",  "SILVER ALERT",
      "89 ANML ENFR",  "ANIMAL ENFORCEMENT CALL",
      "89B ANML BTE",  "ANIMAL BITE",
      "89C AE COMPL",  "ANIMAL ENFORCEMENT COMPLAINT",
      "89F ANML INV",  "ANIMAL ENFORCEMENT FOLLOW UP",
      "89I INJ ANML",  "INJURED ANIMAL",
      "89O OTR ANML",  "OTHER ANIMAL INCIDENT",
      "89T ANML TRP",  "REQUEST FOR ANIMAL TRAP",
      "89W ANM WARN",  "A.E. WARNING ISSUED",
      "90 BURG ALAR",  "BURGLAR ALARM",
      "90B BSN ALRM",  "BUSINESS ALARM",
      "90R RES ALRM",  "RESIDENTIAL ALARM",
      "91 HOSTAGE",    "HOSTAGE SITUATION",
      "92 SURV ACTV",  "SURVEILLANCE ACTIVITY",
      "93 AIR CRASH",  "AIRCRAFT CRASH",
      "94 GUN WOUND",  "GUNSHOT WOUND",
      "95 DRUG PROB",  "DRUG PROBLEM",
      "96 MINI BIKE",  "96 MINI BIKE PROBLEM",
      "97 HOSP REPT",  "HOSPITAL REPORT",
      "98 JUV PROB",   "JUVENILE PROBLEM",
      "BOLO",          "BOLO",
      "C1 ROUTINE",    "ROUTINE",
      "C10 OVERDOSE",  "OVERDOSE",
      "C11A S/KNIFE",  "SUICIDE W/KNIFE",
      "C11B S/GUN",    "SUICIDE W/GUN",
      "C11C S/OTHER",  "SUICIDE OTHER MEANS",
      "C14 IDUST AC",  "INDUSTR ACCI",
      "C15 AUTO ACC",  "AUTO ACCIDENT",
      "C15P PIN IN",   "AUTO ACCIDENT WITH PIN IN",
      "C17 STRU FIR",  "STRUCT FIRE",
      "C18 IND FIRE",  "INDUST FIRE",
      "C19 CHIM FIR",  "CHIMNEY FIRE",
      "C2 URGENT",     "URGENT",
      "C20 AUTO FIR",  "AUTO FIRE",
      "C21 ALL OTHE",  "ALL OTHER",
      "C21A FIR ALR",  "FIRE ALARM",
      "C21AT TEST",    "FIRE ALARM TEST",
      "C21E ELEC FI",  "ELECTRICAL FIRE",
      "C21G GASLEAK",  "GAS LEAK",
      "C21I AST INV",  "ASSIST INVALID",
      "C21L LIFE LN",  "LIFE LINE",
      "C21M MD STB",   "MEDICAL STANDBY",
      "C21P PWR LN",   "POWER LINE",
      "C21PS PUB SV",  "PUBLIC SERVICE",
      "C21R BLKD RD",  "BLOCKED ROAD",
      "C21S SMK INV",  "SMOKE INVST",
      "C21T TREEDOW",  "TREE DOWN",
      "C22 MUL DWE",   "STRUCT.FIRE",
      "C23 BRU FIRE",  "BRUSH FIRE",
      "C23I IL BURN",  "ILLEGAL BURN",
      "C24 DISASTER",  "DISASTER",
      "C24P PLN DWN",  "PLANE CRASH",
      "C25 MUTL AID",  "MUTUAL AID",
      "C3 EMERGENCY",  "EMERGENCY",
      "C33 911 RULE",  "EMERGENCY RULE",
      "C4A ASSAULT",   "ASSAULT",
      "C4B A/W/GUN",   "ASSAULT W/GUN",
      "C4C A/W/KNIF",  "ASSAULT W/KNIFE",
      "C4D A/W/OTH",   "ASSAULT OTHER MEANS",
      "C5 HEART ATK",  "HEART ATTACK",
      "C5A FULL ARR",  "FULL ARREST",
      "C5S STROKE",    "STROKE",
      "C6 SEIZURE",    "SEIZURE",
      "C7 RESP DIST",  "RESP DIST",
      "C8 BRKN BON",   "BROKEN BONE",
      "C9 VIC DWN",    "VICTIM DOWN",
      "C9H HEAD INJ",  "HEAD INJURY",
      "C9S SICKCALL",  "SICK CALL",
      "RADIO PROB",    "RADIO PROBLEM",
      "REPO",          "REPO",
      "TELE PROB",     "TELEPHONE PROBLEM",
      "TEST",          "TEST CALL",
      "TRAFFIC STOP",  "TRAFFIC STOP"
  );
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities & Towns
    "BALFOUR",
    "BARKER HEIGHTS",
    "BAT CAVE",
    "EAST FLAT ROCK",
    "ETOWAH",
    "FLAT ROCK",
    "FLETCHER",
    "FRUITLAND",
    "GERTON",
    "HENDERSONVILLE",
    "HORSE SHOE",
    "LAUREL PARK",
    "MILLS RIVER",
    "MOUNTAIN HOME",
    "MOUNTAIN PAGE",
    "SALUDA",
    "TUXEDO",
    "VALLEY HILL",
    "ZIRCONIA",
    
    // Townships
    "BLUE RIDGE",
    "CLEAR CREEK",
    "CRAB CREEK",
    "EDNEYVILLE",
    "GREEN RIVER",
    "HOOPERS CREEK",
    
    "OUTSIDE COUNTY",
    
    // Buncombe County
    "ARDEN"
  };
}
