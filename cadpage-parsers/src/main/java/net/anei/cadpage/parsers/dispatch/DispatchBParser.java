package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


public class DispatchBParser extends FieldProgramParser {

  int version;

  public DispatchBParser(Properties cityCodes, String defCity, String defState) {
    this(0, cityCodes, defCity, defState);
  }

  public DispatchBParser(String[] cityList, String defCity, String defState) {
    this(0, cityList, defCity, defState);
  }

  public DispatchBParser(String defCity, String defState) {
    this(0, defCity, defState);
  }

  public DispatchBParser(int version, Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState, calcProgram(version));
    this.version = version;
    setup();
  }

  public DispatchBParser(int version, String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState, calcProgram(version));
    this.version = version;
    setup();
  }

  public DispatchBParser(int version, String defCity, String defState) {
    super(defCity, defState, calcProgram(version));
    this.version = version;
    setup();
  }

  private static String calcProgram(int version) {
    switch (Math.abs(version)) {
    case 0: return null;
    case 1: return "CALL_ADDR/SC XS:X? CITY? NAME Return_Phone:PHONE Cad:ID!";
    case 2: return "CALL ADDR Apt:APT? CITY? PLACE Map:MAP Cad:ID";
    case 3: return "CALL ADDR Apt:APT? CITY? NAME Map:MAP Cad:ID";
    case 4: return "CALL! ADDR/S6! Apt:APT? XS:X? CITY? NAME PHONE Map:MAP Cad:ID";
    case 5: return "CALL_ADDR/S69C! Apt:APT? XS:X? CITY? NAME PHONE Map:MAP Cad:ID";
    case 6: return "CALL! ADDR! Apt:APT? XS:X? CITY? APT1? NAME PHONE Map:MAP Cad:ID";
    default:return null;
    }
  }

  private static final String[] FIXED_KEYWORDS = new String[]{"Map", "Grids", "Cad"};
  private static final String[] KEYWORDS =
    new String[]{"Loc", "Return Phone", "BOX", "Map", "Grids", "Cad", "Time"};
  private static final Pattern REPORT_PTN = Pattern.compile("(?:EVENT: *(\\S*?) +LOC:(.*?)[ \n]Cad: +([-0-9]+)[ \n]|\\(?(\\S+?)\\)? *= *)([A-Z0-9]+ +>?\\d\\d:\\d\\d(?::\\d\\d)?[ \n].*)", Pattern.DOTALL);
  private static final Pattern REPORT_PTN2 = Pattern.compile("([-A-Z0-9]+)\n= (DSP .*)");
  private static final Pattern REPORT_DELIM_PTN = Pattern.compile("(?<=\\b\\d\\d:\\d\\d) +(?:Case: \\d+ Disp: *)?");
  private static final Pattern PHONE_PTN = Pattern.compile("(?: +(?:VERIZON|AT ?& ?T MOBILITY))? +(\\d{10}|\\d{7}|\\d{3} \\d{7}|\\d{3}-\\d{4})$");
  private static final Pattern RETURN_PHONE_PTN = Pattern.compile("([-0-9]+) *");
  private static final Pattern TIME_PTN = Pattern.compile("(\\d\\d)(\\d\\d)");
  private static final Pattern INFO_PREFIX_PTN = Pattern.compile("[: ]+");

  @Override
  protected boolean parseMsg(String body, Data data) {

    // Check for run report formats
    Matcher match = REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("CODE ADDR APT ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCode = match.group(1);
      if (data.strCode != null) {
        parseAddress(match.group(2).trim(), data);
        data.strCallId =  match.group(3);
      } else {
        data.strCode = match.group(4);
      }
      data.strSupp = REPORT_DELIM_PTN.matcher(match.group(5).trim()).replaceAll("\n");
      return true;
    }

    match = REPORT_PTN2.matcher(body);
    if (match.matches()) {
      setFieldList("CODE INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCode = match.group(1);
      data.strSupp = REPORT_DELIM_PTN.matcher(match.group(2).trim()).replaceAll("\n");
      return true;
    }

    // See if this is the new fangled line break delimited format
    if (version != 0) {
      String[] flds = body.split("\n");
      if (version > 0 || flds.length >= 3) {
        return parseFields(flds, data);
      }
    }

    // Otherwise use the old logic
    if (! isPageMsg(body)) return false;
    setFieldList("CODE CALL ADDR APT X PLACE CITY NAME PHONE BOX MAP ID TIME");

    body = "Loc: " + body;
    Properties props = parseMessage(body, KEYWORDS);

    String addr = props.getProperty("Loc", "");
    String phone = props.getProperty("Return Phone");
    if (addr.length() == 0) {
      if (phone == null) return false;
      match = RETURN_PHONE_PTN.matcher(phone);
      if (!match.lookingAt()) return false;
      data.strPhone = match.group(1);
      addr = phone.substring(match.end());
    } else {
      if (phone != null) data.strPhone = phone;
    }
    if (!parseAddrField(addr, data)) return false;

    data.strBox = props.getProperty("BOX", "");
    data.strMap = props.getProperty("Map", "");
    String callId = props.getProperty("Cad");
    if (callId != null) {
      parseCallId(callId, data);
    }

    String time = props.getProperty("Time", "");
    match = TIME_PTN.matcher(time);

    if (match.matches()) data.strTime = match.group(1)+':'+match.group(2);
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " INFO";
  }

  /**
   * Determines if this is a CAD page (may be overridden by subclasses)
   * @param body
   * @return
   */
  protected boolean isPageMsg(String body) {
    return isPageMsg(body, FIXED_KEYWORDS);
  }

  /**
   * Processes the complicated first address field
   * Will usually be overridden by subclasses
   * @param field first address field
   * @param data message information data object
   * @return true if parse was successful
   */
  protected boolean parseAddrField(String field, Data data) {

    // Default is to ignore everything up to the first '>'
    int ipt = field.indexOf('>');
    if (ipt >= 0) field = field.substring(ipt+1);
    Matcher match = PHONE_PTN.matcher(field);
    if (match.find()) {
      data.strPhone = match.group(1);
      field = field.substring(0,match.start());
    }
    field = field.replaceAll("//+", "/");
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ, field, data);
    data.strName = cleanWirelessCarrier(getLeft());
    if (data.strName.startsWith("Bldg")) {
      data.strApt = data.strApt + " Bldg";
      data.strName = data.strName.substring(4).trim();
    }
    if (data.strName.equals("UNK")) data.strName = "";
    return true;
  }

  private void parseCallId(String callId, Data data) {
    Matcher match;
    int pt = callId.indexOf(' ');
    if (pt >= 0) {
      String info = callId.substring(pt+1).trim();
      match = INFO_PREFIX_PTN.matcher(info);
      if (match.lookingAt()) info = info.substring(match.end());
      data.strSupp = info;
      callId = callId.substring(0,pt);
    }
    data.strCallId = callId;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("CALL_ADDR")) return new BaseCallAddressField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("APT1")) return new AptField("\\d{1,4}");
    if (name.equals("APT")) return new BaseAptField();
    if (name.equals("MAP")) return new BaseMapField();
    if (name.equals("ID")) return new BaseIdField();
    return super.getField(name);
  }

  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('>');
      if (pt < 0) abort();
      data.strCode = field.substring(0,pt).trim();
      data.strCall = field.substring(pt+1).trim();
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class BaseCallAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.length() == 0) {
        int pt = field.indexOf('>');
        if (pt >= 0) {
          data.strCode = field.substring(0,pt).trim();
          field = field.substring(pt+1).trim();
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE " + super.getFieldNames();
    }
  }

  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = MBLANK_PTN.matcher(field).replaceAll(" ");
      super.parse(field, data);
    }
  }

  private class BaseAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("Bldg");
      if (pt >= 0) field = append(field.substring(pt+4).trim(), "-", field.substring(0,pt).trim());
      super.parse(field, data);
    }
  }

  private static final Pattern NULL_GRID_PTN = Pattern.compile("0*,0*");
  private class BaseMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("Grids:");
      if (pt >= 0) {
        String grid = field.substring(pt+6).trim();
        if (NULL_GRID_PTN.matcher(grid).matches()) grid = "";
        field = field.substring(0,pt).trim();
        field = append(field, "/", grid);
      }
      super.parse(field, data);
    }
  }

  private class BaseIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      parseCallId(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ID INFO";
    }
  }

  private void setup() {
    setupCallList(buildCallList());
  }

  protected CodeSet buildCallList() {
    return new CodeSet(
        "1070 FIRE",
        "1070R RESIDENTIAL FIRE",
        "911 / HANG UP",
        "911 HANG UP",
        "911 HANGUP",
        "911 HANGUP/OPEN LINE",
        "911 HANGUP OR UNVERIFIED",
        "911 HANG UP / OPEN LINE",
        "911 TRANSFER TO ANOTHER COUNTY",
        "ABANDONED VEHICLE",
        "ABD PN - FEM PAIN ABOVE NAV>45",
        "ABDOM PAIN - FEM 12-50 W/FAINT",
        "ABDOM PAIN FAINT/NEAR > 50",
        "ABDOMINAL PAIN",
        "ABDOMINAL PAIN / FAINTING",
        "ABDOMINAL PAIN/PROBLEMS",
        "ABDOMINAL PAINS",
        "ABDOMINAL PAINS CHARLIE",
        "ABDOMINAL/BACK PAIN",
        "ABDOMINAL / BACK PAIN ALS",
        "ABDOMINAL / BACK PAIN BLS",
        "ABDOMINAL PAIN/PROBLEM",
        "ABDOMINAL PAIN BLS P2",
        "ABDUCTION",
        "ABNORMAL BEHAVIOR",
        "ABORT",
        "ABPAIN",
        "ABUSE/ABANDONMENT/NEGLECT",
        "ACCIDENT - BOAT",
        "ACCIDENT - INJURIES",
        "ACCIDENT - NO INJURIES",
        "ACCIDENT INJURIES",
        "ACCIDENT INJURIES UNKNOWN",
        "ACCIDENT MVA WITH INJURIES",
        "ACCIDENT PROPERTY DAMAGE",
        "ACCIDENT W/ INJURIES",
        "ACCIDENT WITH INJURIES",
        "ACCIDENT WITHOUT INJURY",
        "ADVANCED LIFE SUPPORT",
        "ADVANCED LIFE SUPPORT CALL",
        "AGRICULTURAL ACCIDENT",
        "AIRCRAFT DISTRUBANCE",
        "AIRCRAFT EMERGENCY",
        "ALARM ACTIVATION",
        "ALARM-ADVISE TYPE",
        "ALARM/COMMERCIAL",
        "ALARM FIRE",
        "ALARM/FIRE",
        "ALARM/RESID/COMM/PANIC",
        "ALARM/RESIDENCE",
        "ALARM(SPECIFY)",
        "ALARM SYSTEM-NIGHT",
        "ALARM - BURG",
        "ALARM / FIRE",
        "ALARM-HOLDUP",
        "ALARM - MEDICAL",
        "ALARMS",
        "ALLERGIC REACTION",
        "ALLERGIES/ENVENOMATIONS CHARLI",
        "ALLERGY/HIVES/MED REA/STINGS",
        "ALLERGIES/HIVES/STINGS/MED REA",
        "ALL EMS/MEDICAL CALLS",
        "ALL OTHER",
        "ALS",
        "ALTERED LOC",
        "ALTERED MENTAL STATUS",
        "AMBULANCE ALS",
        "ANIMAL BITE",
        "ANIMAL BITES/ATTACKS",
        "ANIMAL CALLS",
        "ANIMAL COMPLAINT",
        "ANIMAL PROBLEM",
        "ANNOUNCMENT",
        "ANYTHING SUSPICIOUS",
        "AOA BOOKING ARREST",
        "ARMED ROBBERY IN PROG.",
        "ARSON FIRE",
        "ASSAULT",
        "ASSULT WITH INJURY",
        "ASSAULT/SEXUAL ASLT",
        "ASSAULT/SEXUAL ASSAULT",
        "ASSAULT/SEXUAL ASSAULT ALPHA",
        "ASSAULT/SEXUAL ASSAULT BRAVO",
        "ASSAULT/SEXUAL ASSAULT CHARLIE",
        "ASSAULT/SEXUAL ASSAULT DELTA",
        "ASSIST",
        "ASSIST A CITIZEN",
        "ASSIST A CITIZEN PASSING ZONE",
        "ASSIST ENROUTE (CLI)",
        "ASSIST OCCUPANT",
        "ASSIST OTHER",
        "ASSIST OTHER AGENCY",
        "ASSIST OTHER TRIBAL PROGRAMS",
        "ASSISTING",
        "ASSISTANCE/ MUTUAL AID",
        "ASTHMA ATTACK",
        "ATTEMPTED SUICIDE",
        "ATTEMPT TO LOCATE",
        "ATV ACCIDENT",
        "ATV CRASH",
        "ATV ON HIGHWAY",
        "AUTO ACCIDENT/INJURY",
        "AUTO ACCIDENT/NO INJURY",
        "AUTO ACCIDENT NO INJURIES",
        "AUTO ACCIDENT WITH INJURIES",
        "AUTO ACCIDENT UNKNOWN",
        "AUTO ACCIDENT UNKNOWN INJURIES",
        "AUTO ALARM/FIRE",
        "AUTO THEFT/TAMPERING",
        "B&E >BURGLARY",
        "BACK PAIN",
        "BACK PAIN (NON-TRAUMATIC)",
        "BACK INJURIES-TRAUMATIC",
        "BASIC LIFE SUPPORT",
        "BASIC LIFE SUPPORT CALL",
        "BEHAVIORAL DISORDER",
        "BLEEDING",
        "BLEEDING (NON-TRAUMATIC)",
        "BLEEDING/NON-TRAUMAT",
        "BLOOD PRESSURE PROB",
        "BLOOD PRESSURE PROBLEMS",
        "BLS",
        "BOAT FIRE/ACCIDENT",
        "BOATER ASSISTS",
        "BOATING COMPLAINTS/VIOLATIONS",
        "BOMB THREAT",
        "BOX ALARM",
        "BOOKING ARREST",
        "BREAK IN",
        "BREATHING ALS",
        "BREATHING DIFFICULTY",
        "BREATHING PROBLEM",
        "BREATHING PROBLEMS",
        "BREATHING PROBLEMS A",
        "BREATHING PROBLEMS / SEVERE",
        "BREATHING PROBLEMS CHARLIE",
        "BREATHING PROBLEMS DELTA",
        "BREATHING PROBLEMS/DIFFICULTY",
        "BREATING PROBLEMS",
        "BRUSH FIRE",
        "BRUSH FIRE NORMAL",
        "BRUSH/FOREST FIRE",
        "BRUSH/GRASS/MULCH/WOODS",
        "BRUSH/RUBBISH FIRE",
        "BTHPRB",
        "BUILDING FIRE",
        "BUILDING SEARCH/OPEN DOOR",
        "BURGLARY/HOME INVASION",
        "BURGLARY IN PROGRESS",
        "BURNS/EXPLOSION",
        "BUSINESS/HOLD UP ALARM",
        "CAD ERRORS/PROBLEMS",
        "CAD SYSTEM TEST",
        "CARBON MONOXIDE DETECTOR ACT",
        "CARBON MONOXIDE/INHAL/HZMT",
        "CARBON MONOXIDE INHALATION",
        "CARBON MONOXIDE INVESTIGATION",
        "CARD ARREST/SUSPECTED WORKABLE",
        "CARDIAC",
        "CARDIAC ARREST",
        "CARDIAC ARREST/DEATH ECHO",
        "CARDIAC OR RESP ARREST/DEATH",
        "CARDIAC OR RESPIRATORY ARREST",
        "CARDIAC PROBLEMS",
        "CARDIAC-OBVIOUS DEATH",
        "CARDIAC/RESP ARREST-INEFF BREA",
        "CARDIAC/RESP ARREST/DEATH",
        "CARDIAC/RESP ARREST / DEATH",
        "CARDIAC/RESPIRATORY ARREST",
        "CARDIAC SYMPTOMS",
        "CHANGE IN MENTAL STATUS",
        "CHECK WELFARE",
        "CHECK WELL BEING",
        "CHEMICAL LEAK",
        "CHEST PAIN",
        "CHEST PAIN ALS",
        "CHEST PAIN - BREATH NORMAL=>35",
        "CHEST PAIN - CARDIAC HISTORY",
        "CHEST PAIN - CLAMMY",
        "CHEST PAIN CHARLIE",
        "CHEST PAIN DELTA",
        "CHEST PAIN - PATIENT NOT ALERT",
        "CHEST PAIN - SEVERE SOB",
        "CHEST PAIN-CLAMMY",
        "CHEST PAIN DIFF SPEAK BTW BRE",
        "CHEST PAIN (NON TRAUMATIC)",
        "CHEST PAIN (NON-TRAUMATIC)",
        "CHEST PAIN/RESPIRATORY DISTRES",
        "CHEST PAINS",
        "CHEST PAINS/RESPITORY",
        "CHILD BIRTH",
        "CHILD LOCKED IN CAR",
        "CHILD LOCKED IN VEHICLE",
        "CHIMNEY FIRE",
        "CITIZEN ASSIST",
        "CIVIL ASSIST",
        "CIVIL COMPLAINT",
        "CIVIL COMPLAINTS",
        "CIVIL MATTER",
        "CIVIL PROCESS",
        "CIVIL STANDBY",
        "CHOKING",
        "CHOKING VICTIM",
        "CODE",
        "CODE 3 CHEST PAIN",
        "CODE 3 DIFFICULTY BREATHING",
        "CODE3 UNCONSCIOUS/UNRESPONSIVE",
        "CO DETECTOR / FIRE RESPONSE",
        "COMMERCIAL FIRE ALARM",
        "COMMERCIAL STRUCTURE FIRE",
        "COMPLIANCE CHECKS",
        "CONFINE SPACE/STRUCTURE COLLAP",
        "CONGESTIVE HEART FAILURE",
        "CONSV BOOKING ARREST",
        "CONVULSIONS/SEIZURES",
        "CONTROL BURN",
        "CONVULSION/SEIZURE",
        "CONVULSIONS/SEIZURES",
        "CONVULSIONS/SEIZURES DELTA",
        "CPR-ADULT",
        "CPR-INFANT",
        "CRIMINAL ABUSE/CHILD",
        "CUSTODY DISPUTE",
        "DAMAGE FROM STORM",
        "DATA DRIVEN ENFORCEMENT PROGRA",
        "DEAD BODY",
        "DEATH",
        "DECEASED PERSON",
        "DECREASED LOC",
        "DIABETIC",
        "DIABETIC ALS",
        "DIABETIC EMRG.",
        "DIABETIC PATIENT",
        "DIABETIC PROB-ALERT/NORM BEHAV",
        "DIABETIC PROBLEM",
        "DIABETIC PROBLEMS",
        "DIABETIC PROBLEMS - NOT ALERT",
        "DIABETIC PROBLEMS CHARLIE",
        "DIABETIC-ALS PRI2",
        "DIABETIC-UNCONSCIOUS",
        "DIFFICULTY BREATHING",
        "DISABLED VEHICLE",
        "DISORIENTED",
        "DISTURBANCE",
        "DIZZINESS",
        "DOA",
        "DOMESTIC",
        "DOMESTIC IN PROGRESS",
        "DOMESTIC DISTURBANCE",
        "DRIVING COMPLAINT",
        "DRIVING UNDER INFLUENCE",
        "DRIVE OFFS",
        "DROWNING",
        "DROWNING/DIVING/SCUBA ACC",
        "DRUG INFO",
        "DRUG OVERDOSE",
        "DRUGS",
        "DUI COMPLAINT",
        "DUMPSTER FIRE",
        "DUMPSTER FIRE NO EXPOSURES",
        "DWELING FIRE",
        "DWELLING FIRE",
        "EDEMA/SWELLING",
        "E_BREATHING ALS",
        "ELEC HAZ/PWR REPT DISCONNECTED",
        "ELECTRICAL FIRE",
        "ELECTRICAL HAZARD",
        "ELECTROCUTION/LIGHTNING",
        "EMD CALL",
        "EMD CODE",
        "EMD PROGRAM",
        "EMERGENACY RUN",
        "EMERGENCY RUN",
        "EMS ASSIST",
        "EMS CALL",
        "EMS LIFELINE CALL",
        "EMS NEEDED",
        "EMS NEEDED PRIORITY 1",
        "EMS NEEDED PRIORITY 2",
        "EMS NEEDED PRIORITY-1",
        "EMS NEEDED PRIORITY-2",
        "EMS STAND BY",
        "EMS TRANSPORT",
        "EMS - ABDOMINAL PAIN",
        "EMS - ALTERED MENTAL STATUS",
        "EMS - ANAPHYLAXIS",
        "EMS - ANXIETY ATTACK",
        "EMS - BLEEDING/LACERATION",
        "EMS - CARDIAC ARREST",
        "EMS - CARDIAC SYMPTOMS",
        "EMS - CHEST PAIN",
        "EMS - CHEST PAINS",
        "EMS - CHF",
        "EMS - CVA",
        "EMS - DEHYDRATION",
        "EMS - DIABETIC COMPLICATIONS",
        "EMS - DIABETIC PROBLEM",
        "EMS - DIFFICULTY BREATHING",
        "EMS - DYSPNEA",
        "EMS - EPISTAXIS",
        "EMS - FALL",
        "EMS - FALL VICTIM",
        "EMS - GENERAL ILLNESS",
        "EMS - HIGH BLOOD PRESSURE",
        "EMS - HYPERTENSION",
        "EMS - ILL PERSON",
        "EMS - IMMEDIATE TRANSPORT",
        "EMS - INJURY / EXTREMITY",
        "EMS - LIFTING ASSISTANCE",
        "EMS - MEDICAL ALARM",
        "EMS - MEDICAL EMERGENCY",
        "EMS - NAUSEA / VOMITING",
        "EMS - OVERDOSE",
        "EMS - PAIN",
        "EMS - PAIN, ALL OVER",
        "EMS - PASSED OUT",
        "EMS - PREGNANCY, UNKNOWN PAIN",
        "EMS - ROUTINE TRANSPORT",
        "EMS - SEIZURE",
        "EMS - SEIZURES",
        "EMS - SEMI-RESPONSIVE",
        "EMS - SHORTNESS OF BREATH",
        "EMS - STOMACH PAINS",
        "EMS - STROKE",
        "EMS - SUICIDE / COMPLETED",
        "EMS - UNCONSCIOUS",
        "EMS - UNRESPONSIVE PERSON",
        "EMS - UNKNOWN",
        "EMS - UNKNOWN CONDITIONS",
        "EMS - VERTIGO, DIZZY",
        "EOC PROJECT",
        "EPO/DVO VIOLATION",
        "E SICK/UNKNOWN ALS",
        "ESCAPE",
        "ESCORT",
        "EXERCISE",
        "EXPLOSION",
        "EXTRA PATROL",
        "EXTRICATION",
        "EYE INJURY",
        "EYE PROBLEM/INJURIES",
        "F_ACCIDENT NO INJURIES",
        "F-ACCIDENT W/ INJURIES",
        "FALL",
        "FALL VICTIM",
        "FALL INJURY BLS",
        "FALL - ANY",
        "FALL-NOT DANGEROUS BODY AREA",
        "FALLS",
        "FALLS (GRND/FLOOR) UNK STATUS",
        "FALLS BRAVO",
        "FALLS DELTA",
        "FALLS/ACCIDENTS BLS",
        "FALLS-ALS PRI1-FR",
        "FALLS/BACK INJURY (TRAUMATIC)",
        "FALLS/ BACK INJURIES-TRAUMATIC",
        "FALLS-BLS PRI2",
        "FALLS-ON GRD/FL-NOT DANGER BOD",
        "FALLS (GRD/FLR) POSS DANGER BO",
        "FALLS - NOT ALERT",
        "FATAL TFC ACCIDENTS",
        "FIELD/WOOD FIRE",
        "FIGHT",
        "FIGHT-IN PROGRESS",
        "FIGHT IN PROGRESS",
        "FIRE",
        "FIRE / CHIMNEY",
        "FIRE / WILDFIRE",
        "FIRE AGRICULTURE",
        "FIRE ALARM",
        "FIRE ALARM COMMERCIAL/INDUST",
        "FIRE ALARM-RESIDENTIAL",
        "FIRE ALARM TEST/WORK",
        "FIRE BRUSH",
        "FIRE CALL",
        "FIRE CARBON MONOXIDE ALARM",
        "FIRE DEPT CALL",
        "FIRE DEPARTMENT CALL",
        "FIRE EXPLOSION",
        "FIRE GAS\\PROPANE LEAK",
        "FIRE GENERIC (TYPE)",
        "FIRE GRASS OR WOODS",
        "FIRE INDUSTRIAL",
        "FIRE INVESTIGATION",
        "FIRE INVESTIGATION INSIDE",
        "FIRE INVESTIGATION OUTSIDE",
        "FIRE OTHER",
        "FIRE POWERLINE",
        "FIRE POWER LINE DOWN",
        "FIRE PRE-ALERT",
        "FIRE RESIDENTIAL",
        "FIRE SERVICE CALL",
        "FIRE SMOKE INVESTIGATION",
        "FIRE STANDBY",
        "FIRE STRUCTURE",
        "FIRE STRUCTURE HOUSE OR BUSN",
        "FIRE STRUCTURE HOUSE OR BUSINE",
        "FIRE TRANSFORMER / WIRES",
        "FIRE TREE DOWN",
        "FIRE VEHICLE",
        "FIRE WILD",
        "FIRE (UNKNOWN ORGIN)",
        "FIRE - AUTO ALARM/FIRE",
        "FIRE - ALARM",
        "FIRE - BARN / LARGE INDUSTRIAL",
        "FIRE - BRUSH FIRE",
        "FIRE - CONTROLLED BURN",
        "FIRE - ELECTRICAL FIRE",
        "FIRE - FUEL SPILL/LEAK",
        "FIRE - HELICOPTER LANDING ZONE",
        "FIRE - INSPECTION",
        "FIRE - INVESTIGATION",
        "FIRE - LINES DOWN (NOT-ELEC.)",
        "FIRE - STRUCTURE",
        "FIRE - STRUCTURE / HOUSE, BLDG",
        "FIRE - STRUCTURE/RESIDENTIAL",
        "FIRE - TREE DOWN",
        "FIRE - TREES DOWN",
        "FIRE - UNKNOWN FIRE",
        "FIRE - UNKNOWN SMOKE",
        "FIRE - VEHICLE",
        "FIRE - VEHICLE FIRE",
        "FIRE - WILDFIRE",
        "FIRE / STRUCTURE",
        "FIRE / VEHICLE",
        "FIRE / WILDFIRE",
        "FIRE/ARSON",
        "FIRE/GENERAL ALARM-COMM STRUC",
        "FIRE/PROQA GENERATED",
        "FIRE/WILDFIRE",
        "FIRST RESPONDER",
        "FLOODING / BASEMENT",
        "FLU LIKE SYMPTOMS",
        "FOLLOW UP",
        "FOREST FIRE",
        "FORGERY",
        "FORGERY/COUNTERFEITING",
        "FOR INFORMATIONAL PURPOSES",
        "FRACTURE",
        "FRACTURE-BONE",
        "F_TREE DOWN",
        "F_TREE DOWN BUENA",
        "FRAUD",
        "FUEL SPILL 10 GAL OR LESS",
        "GAS EVENT",
        "GAS LEAK",
        "GAS ODOR/OUTSIDE",
        "GENERAL ILLNESS",
        "GENERAL PAIN",
        "GI PROBLEM",
        "GRASS FIRE CALL",
        "GRASS/WOODS FIRE",
        "GUN/WEAPON PERSON WITH",
        "HARASSMENT",
        "HARRASMENT/TELEPHONE",
        "HAZARD",
        "HAZARDOUS MATERIAL",
        "HAZARDOUS MATERIALS",
        "HAZMAT",
        "HAZMAT/SPILL/LEAK/GAS EMERG",
        "HEADACHE",
        "HEADACHE CHARLIE",
        "HEADACHE SUDDEN ONSET SEVERE P",
        "HEAD INJURY",
        "HEART ATTACK",
        "HEART PROB/AICD - UNK STATUS",
        "HEART PROBLEM",
        "HEART PROBLEMS",
        "HEART PROBLEMS/AICD",
        "HEART PROBLEMS/AICD CHARLIE",
        "HEART PROBLEMS/AICD DELTA",
        "HEAT/COLD EXPOSURE",
        "HEM/LACER-SERIOUS",
        "HEMMORAGE",
        "HEMORRHAGE BLS",
        "HEMORRAGE/LACERATION",
        "HEMORRHAGE / LAC - DANGER HEM",
        "HEMORRHAGE / LACERATIONS",
        "HEMORRHAGE/LACERATIONS",
        "HEMORRHAGE/LACERATIONS BRAVO",
        "HEMORR/LACER - SERIOUS HEMORRH",
        "HIT AND RUN",
        "HIT & RUN JUST OCC.",
        "HGH LIFE HZD ALRM GEN",
        "HOLD UP ALARM IP",
        "HOUSE FIRE",
        "ILLEGAL BURN",
        "ILLEGAL BURNING INCIDENTS",
        "ILLEGAL GAMBLING",
        "ILL PERSON",
        "ILLPERSON",
        "ILL PERSON ALS",
        "ILL PERSON BLS",
        "IMMEDIATE TRANSPORT",
        "INDECENT EXPOSURE",
        "INDUSTRIAL/MACHINERY ACCIDENT",
        "INFORMATIONAL",
        "INHALATION/HAZ-MAT",
        "INFORMATION REPORT",
        "INJURED PERSON",
        "INJURED PERSON ALS",
        "INJURED PERSON BLS",
        "INJURY",
        "INJURY ACCIDENT",
        "IN PROGRESS OR OCCURRED",
        "INTOX PERSON",
        "INTOXICATED PERSON",
        "INVESTIGATE SMOKE",
        "INVESTIGATE WEATHER DAMAGE",
        "INVESTIGATION FOLLOW-UP",
        "JUVENILE BEYOND CONTROL",
        "KEYS - LOCKED OUT - RESIDENCE",
        "KIDNAPPING",
        "KIDNAPPING/ATTEMPT",
        "KIDNAPPING REPORT",
        "LACERATION",
        "LAND FIRE",
        "LANDING ZONE",
        "LAW - DISPUTE / FAMILY",
        "LAW EVENT",
        "LAW/POLICE PROQA",
        "LEG INJURY / PAIN",
        "LIFE CALL UNKNOWN PROBLEM",
        "LIFELINE ACTIVATION",
        "LIFT ASSIST",
        "LIFT ASSISTANCE",
        "LIFT ASSIST/NON EMER EMS",
        "LIFTING ASSISTANCE",
        "LINES DOWN",
        "LINES DOWN POWER/PHONE/CABLE",
        "LITTERING",
        "LIVESTOCK",
        "LIVESTOCK LOST/STOLEN",
        "LIVESTOCK ON ROADWAY",
        "LOCKED OUT (CAR & HOME)",
        "LOITERING",
        "MAN DOWN",
        "MANUFACTURE CONT SUBSTANCE",
        "MARIJUANA ERADICATION - CULT",
        "MARINE FIRE",
        "MARINE THEFT",
        "MATERNITY",
        "MVA-ALS PRI1",
        "M.V.A. - POSSIBLE INJURIES",
        "MED PRE-ALERT",
        "MEDICAL",
        "MEDICAL ALARM",
        "MEDICAL ALARM ACTIVATION",
        "MEDICAL ALPHA",
        "MEDICAL BRAVO",
        "MEDICAL CALL",
        "MEDICAL CALL PROQA",
        "MEDICAL CALL - UNKNOWN",
        "MEDICAL CHARLIE",
        "MEDICAL DELTA",
        "MEDICAL ECHO",
        "MEDICAL CALL",
        "MEDICAL EMERGENCY",
        "MEDICAL GENERIC",
        "MEDICAL SERVICE CALL-NON EMERG",
        "MED GENERIC DO NOT DELETE",
        "MEET COMPLAINANT",
        "MEET COMPLAINTANT",
        "MENTAL/EMOTIONAL/PS",
        "MENTAL/EMOTIONAL/PSYCHOLOGICAL",
        "MENTALLY ILL PERSON",
        "MENTAL PATIENT FOR TRANSPORT",
        "MENTAL PERSON",
        "MENTAL PETITION ETC",
        "MESSAGE INFORMATION REF TRIBAL WARRANTS",
        "MISC",
        "MISC FIRE",
        "MISC. FIRE",
        "MICELLANEOUS CALL FOR SERVICE",
        "MISCELLANEOUS COMPLAINT",
        "MISCELLANEOUS EMS CALLS",
        "MISCELLANEOUS TRAFFIC COMPLNTS",
        "MISSING PERSON",
        "MISSING PERSON / RUNAWAY",
        "MISSING PERSON/UNDER 18YOA/JUV",
        "MISSING/RUNAWAY/FOUND PERSON",
        "MOTOR VEC ACCIDENT NO INJURIES",
        "MOTOR VEH ACCID PRIVATE LOT",
        "MOTOR VEH ACC UNKNOWN INJ",
        "MOTOR VEH ACC W/INJ",
        "MOTOR VEHICLE ACCIDENT",
        "MOTOR VEHICLE ACCIDENT INJ",
        "MOTOR VEHICLE ACCIDENT INJURY",
        "MOTOR VEHICLE ACCIDENT NEED FD",
        "MOTOR VEHICLE ACCIDENT UNK INJ",
        "MOTOR VEHICLE ACCIDENT W/INJUR",
        "MOTOR VEHICLE COLLISION",
        "MOTORIST ASSIST",
        "MSHP BOOKING ARREST",
        "MSWP BOOKING ARREST",
        "MURDER",
        "MUT AID",
        "MUTUAL AID",
        "MUTUAL AID/ASSIST OUTSIDE AGEN",
        "MUTUAL AID FIRE RESPONSE",
        "MUTUAL AIDE",
        "MV ACCIDENT W/INJURY",
        "MVA",
        "MVA LEAVING THE SCENE",
        "MVA NO INJURIES",
        "MVA NO INJURY",
        "MVA NONE INJURY",
        "MVA W/ RESCUE",
        "MVA W/INJURIES",
        "MVA WITH INJURIES",
        "MVA WITH INJURY",
        "MVA WITH INJURY & ENTRAPMENT",
        "MVA UNKNOWN INJURY OR ENTRAP",
        "MVA WITH UNKNOW INJUIRIES",
        "MVA -EJECTION- HIGH MECHANISM",
        "MVA - MAJOR INCIDENT",
        "MVA - PINNED",
        "MVA - PINNED / ENTRAPPED",
        "MVA - UNKNOWN STATUS",
        "MVA - WITH INJURIES",
        "MVA-ALS PRI1",
        "MVA-TRAPPED MULTI PT/ADD RESPO",
        "MVC NO INJURIES REPORTED",
        "MVC UNKNOWN INJURIES",
        "NATURAL OR LP GAS LEAK",
        "NATURAL GAS ODOR/LEAK",
        "NEED ASSISTANCE",
        "NO INJURY ACCIDENT",
        "NON-INJURY ACCIDENT",
        "NON INJURY ACCIDENT",
        "NON EMERG TRANSPORT",
        "NON EMERG TRANSPORT DPD///TEST",
        "NON EMERG TRANSPORT DPD/TEST",
        "NON EMERGENCY RUN",
        "NON-SPECIFIC DIAGNOSIS",
        "NO OPERATORS LICENSE",
        "NOT REGULAR CALL",
        "OB CALL",
        "OBSCENE/THREATENING PHONE",
        "ODOR",
        "ODOR INVESTIGATION/COMPL",
        "ODOR / OTHER THAN SMOKE",
        "ODOR/OTHER THAN SMOKE",
        "OFFICER BUSY",
        "ORDINANCE VIOLATION",
        "OTHER EVENT NOT DEFINED",
        "OTHER-FIRE",
        "OTHER FIRE CALL",
        "OVERDOSE",
        "OVERDOSE/INJESTION/POISONING",
        "OVERDOSE/POISONING",
        "PAIN GENERAL",
        "PARK CHECK",
        "PATIENT ASSIST",
        "PATIENT EVALUATION",
        "PATIENT TRANSPORT",
        "PATROL REQUEST N45",
        "PEACE DISTURBANCE",
        "PEDSTRIAN HIT",
        "PEDESTRIAN STRUCK BY VEHICLE",
        "PERSONAL INJURY ACCIDENT",
        "PERSON DOWN",
        "PHONE",
        "PHONE CALL",
        "PLANE MALFUNCTION",
        "POISONING",
        "POSS OF CONTROLLED SUBSTANCE",
        "POSSESSION OF ALCOHOL BY MINOR",
        "POSSIBLE DOA",
        "POSSIBLE DUI",
        "POSSIBLE OPEN BURN",
        "POST SURGICAL COMPLICATION",
        "POWER/CABLE/PHONE LINES DOWN",
        "POWER LINES DOWN",
        "POWER OUTAGE",
        "PREGNANCY/CHILDBIRTH/MISCARR",
        "PREGNANCY/CHILDBIRTH/MISCARIAG",
        "PRISONER TRANSPORT",
        "PROBLEM UNKNOWN",
        "PROCESS SERVICE",
        "PROPERTY DAMAGE/VANDALISM",
        "PROPERTY DISPUTE",
        "PROWLER",
        "PSYC/ABN BEHAV/SUICIDE",
        "PSYC/ABN BEHAV/SUICIDE DELTA",
        "PSYCH/ABNORM BEH/SUICIDE OVERR",
        "PSYCHIATRIC/SUICIDE ATTEMPT",
        "PUBLIC ASSIST",
        "PUBLIC SERVICE",
        "PURSUIT",
        "RAPE",
        "RAPE/ATTEMPT RAPE",
        "REC/RECOVER STOLEN PROPERTY",
        "RECKLESS DRIVER",
        "REFER TO OUTSIDE AGENCY",
        "REFINERY/TANK FARM/FUEL STORAG",
        "REKINDLE",
        "REPORTED SUICIDE",
        "REQUEST FOR TOW LAND/WATER",
        "RES (SINGLE) HEAT DETECTOR",
        "RESCUE",
        "RESCUE NOT MVA",
        "RESCUE - AUTO ACC / ENTRAPMENT",
        "RESCUE - AUTO ACCIDENT/ INJURY",
        "RESCUE - AUTO ACCIDENT/INJURY",
        "RESCUE (TYPE)",
        "RESCUE/ANIMAL RESCUE",
        "RESCUE/ELEVATOR EMERGENCY",
        "RESIDENTIAL FIRE",
        "RESIDENTIAL STRUCTURE FIRE",
        "RESPIRATORY DISTRESS",
        "ROAD BLOCK",
        "ROAD BLOCKAGE OF ANY TYPE",
        "ROAD CLOSING",
        "ROAD HAZARD",
        "ROAD HAZZARD",
        "ROBBERY/CARJACKING",
        "ROBBERY OCCURED EARLIER",
        "ROCK SLIDE",
        "ROUTINE TRANSPORT",
        "RUBBISH FIRE NO EXPOSURES",
        "RUNAWAY JUV",
        "RUNAWAY JUVENILE",
        "RUNWAY JUVENILE",
        "SCHOOL VISITS",
        "SEARCH AND RESCUE",
        "SEXUAL ABUSE CHILD",
        "SEXUAL MISCONDUCT",
        "SEX OFFEND REG FAIL TO COMPLY",
        "SEIZURE",
        "SEZIURE\\CONVULSION",
        "SEIZURES",
        "SEIZURES ALS",
        "SEIZURES - NOT CURRENT NOT VER",
        "SERVICE CALL",
        "SERVICE CALL/ALL OTHER/SPECIFY",
        "SEVERE WEATHER REPORTS/DAM",
        "SHOOTING",
        "SHOPLIFTING",
        "SHORTNESS OF BREATH",
        "SICK",
        "SICK - CARDIAC",
        "SICK PERSON",
        "SICK PERSON-ALT LVL OF CONSC",
        "SICK PERSON-NEW ONSET IMMOBILI",
        "SICK PERSON ALPHA RESPONSE",
        "SICK PERSON CHARLIE",
        "SICK PERSON (SPECIFIC DIAG)",
        "SICK PERSON- DIZZINESS/VERTIGO",
        "ICK PERSON-SPECIFIC DIAGNOSIS",
        "SICK PERSON-SPECIFIC DIAGNOSIS",
        "SICK PERSON - NAUSEA",
        "SICK PERSON - NOT ALERT",
        "SICK PERSON W/ NO PRIORITY SYP",
        "SICK-ALTERED LEV OF CONSCIOUS",
        "SICK - NO SYMPTOMS",
        "SICK UNKNOWN",
        "SICK/UNKNOWN",
        "SICK/UNKNOWN ALS",
        "SILENT ALARM",
        "SIK PERSON",
        "SIG 12",
        "SMELL OF SMOKE",
        "SMOKE",
        "SMOKE IN THE AREA",
        "SMOKE INV/OUTSIDE HEAVY SMOKE",
        "SMOKE INVESTIGATION",
        "SMOKE INVEST/SMELL SMOKE",
        "SPECIAL ASSIGNMENT",
        "SPECIFY -(I.E. -ARM/LEG)",
        "SPECIFY-RUN OFF/HIGH",
        "SPILL (TYPE)",
        "SOCIAL SERVICE REFERRALS",
        "S/SMOK",
        "STAB/GUNSHOT WOUND/PENET TRAUM",
        "STABBING",
        "STABBING ON",
        "STALKING",
        "STEALING",
        "STAND BY FOR FIRE COVERAGE",
        "STR FIR HGH RISE",
        "STROKE",
        "STROKE-ABNORMAL BREATH <2HRS",
        "STROKE(CVA)",
        "STROKE-CVA",
        "STROKE (CVA)",
        "STROKE (CVA) - NOT ALERT <2HRS",
        "STROKE (CVA) CHARLIE",
        "STROKE-NOT ALERT",
        "STOKE(CVA)",
        "STROKE/CVA",
        "STROKE/CVA PATIENT",
        "STROKE (CVA) BREATH NORM > 35",
        "STRUC FIRE-SINGLE RESIDENTIAL",
        "STRUCTURE FIRE",
        "STRUCTURE FIRE CHARLEY RESPONS",
        "STRUCTURE FIRE-COMMERCIAL",
        "STRUCTURE FIRE-BARN/GARAGE/OTH",
        "STRUCTURE FIRE DELTA RESPONSE",
        "STRUCTURE FIRE - LOW HAZARD",
        "STRUCTURE FIRE-MEDIUM HAZARD",
        "STRUCTURE FIRE - HIGH HAZARD",
        "STRUCTURE FIRE- HIGH RISE",
        "STRUCTURE FIRE-MULTIFAMILY",
        "STRUCTURE FIRE-RESIDENTAL",
        "STRUCTURE FIRE W ENTRAPMENT",
        "SUBJECT ILL",
        "SUICIDE",
        "SUICIDE ATTEMPT",
        "SUICIDE--ATTEMPT",
        "SUICIDE ATTEMPT OR THREAT",
        "SUICIDE OR ATTEMPTED SUICIDE",
        "SUICIDE THREAT",
        "SUICIDAL PERSON/ATTEMPTED SUIC",
        "SURVEILLANCE/SPEC DETAILS",
        "SUSPICIOUS ACTIVITY",
        "SUSPICIOUS INCIDENT",
        "SUSPICIOUS PERSON",
        "SUSPICIOUS PERSON/VEHICLE",
        "SUSPICIOUS VEHICLE",
        "SUSPICIOUS VEHICLE STOP",
        "SUSP INDIVIDUAL ON SCENE (CLI)",
        "SWELLING",
        "SWIFT/FLOOD WATER RES",
        "SYNCOPE",
        "SYNCOPAL EPISODE (FAINTING)",
        "TERRORISTIC THREATENING",
        "TEST",
        "TEST CALL",
        "THEFT COMPLAINT",
        "THEFT/LARCENY",
        "THEFT OF MEDICATION",
        "THREAT",
        "THREATS",
        "TRAFAC",
        "TRAFFIC ACCIDENT",
        "TRAFFIC ACCIDENTS",
        "TRAFF OR TRANSPT ACC/MVA W/INJ",
        "TRAFFIC CHECKPOINT",
        "TRAFFIC CONTRO",
        "TRAFFIC CONTROL",
        "TRAFFIC HAZARD",
        "TRAFFIC PROBLEM",
        "TRAFFIC PURSUIT",
        "TRAFFIC STOP",
        "TRAFFIC/TRANSPORTATION ACCIDEN",
        "TRAFFIC VIOL/COMPLAINT/HAZARD",
        "TRAILER FIRE",
        "TRAIN/RAIL ACCIDENT",
        "TRANSFORMER",
        "TRAUMA INJ-POSS DANGER BODY AR",
        "TRAUMATIC INJ - NOT RECENT",
        "TRAMATIC INJURY",
        "TRAUMATIC INJURIES",
        "TRAUMATIC INJURIES - SPECIFIC",
        "TRANSFERED TO HP",
        "TRASH FIRE",
        "TRAUMA",
        "TRAUMA WITH INJURY",
        "TRAUMATIC INJURY",
        "TRAUMA INJURY-NOT DANGER BODY",
        "TRANSFORMER INCIDENT",
        "TRANSPORT/ INTERFACILITY/",
        "TRANSPORT PATIENT",
        "TRANSPORTING PRISONER",
        "TRAUMATIC INJ - DANGEROUS",
        "TRAUMATIC INJURIES (SPECIFIC)",
        "TREE DOWN",
        "TREE_DOWN",
        "TREES DOWN",
        "TREE IN ROADWAY",
        "TREES/WIRES DOWN URGENT",
        "TRESPASSING",
        "TRFC/HIGH MECHANISM",
        "TRY TO CONTACT",
        "UNCONCIOUS",
        "UNCONCIOUS PERSON",
        "UNCONSCIOUS",
        "UNCONSCIOUS CHANGING COLOR",
        "UNCONSCIOUS EFFECT BREATHING",
        "UNCONSCIOUS/FAINTING",
        "UNCONSCIOUS/FAINTING (NEAR)",
        "UNCONSCIOUS/FAINTING DELTA",
        "UNCONSCIOUS/FAINTING NONTRAUMA",
        "UNCONSCIOUS/FAINTING-NONTRAUMA",
        "UNCONSCIOUS PATIENT",
        "UNCONSCIOUS PERSON",
        "UNCONSCIOUS/SYNCOPE",
        "UNCONSCIOUS SUBJECT",
        "UNCONSCIOUS/UNRESPONSIVE",
        "UNCONTAINED HAZMAT",
        "UNK PROBLEM-LIFE STAT QUESTION",
        "UNKNOWN",
        "UNKNOWN - ALERT",
        "UNKNOWN MEDICAL",
        "UNKNOWN PROB (MAN DOWN)",
        "UNKNOWN PROB (MAN DOWN) BRAVO",
        "UNKNOWN PROB (MAN DOWN) DELTA",
        "UNKNOWN PROBLEM",
        "UNKNOWN PROBLEM (PERSON DOWN)",
        "UNKNOWN PROBLEM (MAN DOWN)",
        "UNKNOWN PROBLEM-MAN DOWN",
        "UNKNOWN PROBLEMS",
        "UNKNOWN SITUATION",
        "UNKNOWN/PERSON DOWN",
        "UNRESPONSIVE",
        "UNRESPONSIVE / SYNCOPE ALS",
        "UNRESPONSIVE / SYNCOPE BLS",
        "UNRSPONSIVE / SYNCOPE ALS",
        "UTILITY TROUBLE/EMERGENCIES",
        "VA / INJURIES",
        "VEHICLE ACCIDENT",
        "VEHICLE COLLISION/W INJURIES",
        "VEHICLE COLLISION /W INJURIES",
        "VEHICLE FIRE",
        "VEHICLE PURSUIT",
        "VEHICLE- DISABLED",
        "WALKAWAY FROM HOSPITAL/HOME",
        "WANTED PERSON",
        "WARRANT SERVICE",
        "WARRANT SERVICE / FELONY",
        "WATER RESCUE",
        "WATER/ICE RESCUE",
        "WEAPONS/FIREARMS",
        "WEATHER SPOTTERS",
        "WELFARE CHECK",
        "WELFARE CHECK CHILD FOUND ON DOOR STEP ON",
        "WILDLAND FIRE",
        "WILD LAND FIRE",
        "WILD LAND FIRE OWLE",
        "WILD FIRE",
        "WIRE DOWN",
        "WIRE (GENERAL PROBLEM)",
        "WIRES",
        "WIRES/POLE",
        "WIRES DOWN",
        "WIRE(S) DOWN",
        "WIRES DOWN / ARCING",
        "WIRES/TRANSFORMER DOWN FIRE",
        "WRECK",
        "WOODLAND FIRE INCIDENTS"
    );
  };
}
