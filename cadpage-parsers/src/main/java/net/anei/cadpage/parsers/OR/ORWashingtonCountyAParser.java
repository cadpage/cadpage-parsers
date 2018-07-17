package net.anei.cadpage.parsers.OR;

import java.util.Arrays;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

/**
 * Washington County, OR
 * Also Clackamas County
 */
public class ORWashingtonCountyAParser extends ORWashingtonCountyBaseParser {
  
  private String version;

  public ORWashingtonCountyAParser() {
    this("WASHINGTON COUNTY", "OR");
  }
  
  public ORWashingtonCountyAParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState,
          "( SELECT/1 ADDR1/SC! MAP:MAP UNIT:UNIT! | " +
            "ADDRCITY SRC! ( MAP:MAP X2 | ) DASH? PLACE2 NAME2! )");
    setupCities(ORWashingtonCountyParser.CITY_LIST);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSpecialStreets("E ST", "HWY 99E", "HWY 99W");
  }
  
  @Override
  public String getAliasCode() {
    return "ORWashingtonCountyA";
  }
  
  @Override
  public String getFilter() {
    return "930010,777,888,wccca@wccca.com,majcs@majcs.us";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public int splitBreakLength() { return 230; }
      @Override public int splitBreakPad() { return 1; }
   };
  }
  
  private static final Pattern BAD_SUBJECT_PTN = Pattern.compile("PW(\\d+)|VisiCAD Email");
  
  private static final Pattern RECALL_PTN = Pattern.compile("(RECALL):(.*) C\\*\\d{5}(?:CANCEL|RECALL)?(.*)");
  private static final Pattern RECALL_CITY_PTN = Pattern.compile("(.*),([A-Z]{3}) (.*)");
  
  private static final Pattern MASTER_PTN1 = Pattern.compile("([A-Z0-9]+) - ([-A-Z0-9]{2,5}) \\((.*?)\\) (.*?)#([A-Z]{2}\\d+)\\b[, ]*(.*)");
  
  private static final Pattern MASTER_PTN3 = Pattern.compile("(?:([A-Z]{2,5}\\d\\d-\\d{7}) )?[Cc]all for (?:([-A-Z0-9]{2,5}) - )?(.*?)(?: +|(?<=[A-Z]))at(?: +|(?=[A-Z0-9]))(.*?)(?: cross streets *(.*?))? ?Units resp[. ]+([A-Z0-9,]+) *(?:time: ?(\\d\\d:\\d\\d)Inc#([A-Z]*\\d+)(?: Apt(.*?))?(?:City(.*?))?(?: Lat(\\d{8,}) Lon(\\d{8,}))?|(\\[.*))");
  
  private static final Pattern MASTER_PTN5 = Pattern.compile("([A-Z]{2,6}\\**) - (.*?) Units ([,A-Z0-9]+) RP +(?:(.*?) +)?Comment(.*)");
  
  private static final Pattern NAME_PHONE_PTN = Pattern.compile("(.*?) *(\\d{3}[- ]\\d{3}[- ]\\d{4})");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Reject anything for ORWashingtonCountyB or ORWashingtonCountyC
    if (BAD_SUBJECT_PTN.matcher(subject).matches()) return false;
    
    body = stripFieldStart(body, "*");
    body = stripFieldEnd(body, "\\");
    
    // Special recall format
    Matcher match = RECALL_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ADDR APT CITY UNIT");
      data.strCall = append(match.group(1), " - ", match.group(3).trim());
      body = match.group(2).trim();
      match = RECALL_CITY_PTN.matcher(body);
      if (match.matches()) {
        parseAddress(match.group(1).trim(), data);
        data.strCity = convertCodes(match.group(2), CITY_CODES);
        data.strUnit = match.group(3).trim();
      } else {
        parseAddress(StartType.START_ADDR, body, data);
        data.strUnit = getLeft();
      }
      return true;
    }
    
    // Another alternate format
    match = MASTER_PTN1.matcher(body);
    if (match.matches()) {
      version = "2";
      data.strUnit = match.group(1);
      data.strCode = match.group(2);
      data.strCall = match.group(3).trim();
      body = match.group(4);
      data.strCallId = match.group(5);
      data.strSupp = match.group(6);
      
      if (body.endsWith("-")) body += ' ';
      return parseFields(body.split("- ", -1), data);
    }
    
    // They keep on coming
    match = MASTER_PTN3.matcher(body);
    if (match.matches()) {
      setFieldList("ID CODE CALL ADDR APT X UNIT TIME CITY GPS INFO");
      data.strCallId = getOptGroup(match.group(1));
      data.strCode = getOptGroup(match.group(2));
      data.strCall = expandCall(match.group(3).trim());
      parseAddress(match.group(4).trim(), data);
      data.strCross = cleanCross(getOptGroup(match.group(5)));
      data.strUnit = match.group(6).trim();
      data.strTime = getOptGroup(match.group(7));
      data.strCallId = append(data.strCallId, "/", getOptGroup(match.group(8)));
      data.strApt = append(data.strApt, "-", getOptGroup(match.group(9)));
      data.strCity = getOptGroup(match.group(10));
      String gps1 = match.group(11);
      String gps2 = match.group(12);
      if (gps1 != null) setGPSLoc(convertGPS(gps1)+','+convertGPS(gps2), data);
      data.strSupp = getOptGroup(match.group(13));
      return true;
    }
    
    match = MASTER_PTN5.matcher(body);
    if (match.matches()) {
      setFieldList("CODE CALL ADDR APT UNIT NAME PHONE INFO");
      data.strCode = match.group(1);
      String callAddr = match.group(2).trim();
      data.strUnit = match.group(3);
      String namePhone = getOptGroup(match.group(4));
      data.strSupp = match.group(5).trim();
      
      int pt = callAddr.indexOf(" at ");
      if (pt >= 0) {
        data.strCall = expandCall(callAddr.substring(0, pt).trim());
        parseAddress(callAddr.substring(pt+4).trim(), data);
      } else {
        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, callAddr, data);
      }
      
      match = NAME_PHONE_PTN.matcher(namePhone);
      if (match.matches()) {
        namePhone = match.group(1);
        data.strPhone = match.group(2);
      }
      data.strName = namePhone;
      
      return true;
    }

    version = "1";
    body = body.replace(" UNITS:", " UNIT:");
    return super.parseMsg(body, data);
  }
  
  private String cleanCross(String cross) {
    cross = cross.replace("No X Street", "").trim();
    cross = stripFieldStart(cross, "/");
    cross = stripFieldEnd(cross, "/");
    return cross;
  }
  
  private static String convertGPS(String field) {
    int pt = field.length()-6;
    if (field.length() > 6) field = field.substring(0, pt) + '.' + field.substring(pt);
    return field;
  }
  
  @Override
  public String getProgram() {
    String result = super.getProgram();
    if (version != null && version.equals("2")) result = "UNIT CODE CALL " + result + " ID INFO";
    return result;
  }
  
  @Override
  protected String getSelectValue() {
    return version;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR1")) return new MyAddressField();
    if (name.equals("X2")) return new MyCross2Field();
    if (name.equals("DASH")) return new SkipField("-", true);
    if (name.equals("PLACE2")) return new MyPlace2Field();
    if (name.equals("NAME2")) return new MyName2Field();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([-A-Z0-9]{2,6}) - +(.*)");
  private static final Pattern MSPACE_PTN = Pattern.compile(" {3,}");
  private static final Pattern DIR_OF_SLASH_PTN = Pattern.compile("\\b([NSEW] *OF) */ *");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      
      int flags = FLAG_START_FLD_REQ | FLAG_NO_IMPLIED_APT | FLAG_ANCHOR_END;
      int pt1 = field.lastIndexOf(')');
      if (pt1 >= 0) {
        data.strCity = convertCodes(field.substring(pt1+1).trim(), CITY_CODES);
        int pcnt = 1;
        int pt2 = pt1-1;
        for (; pt2 >= 0; pt2--) {
          char chr = field.charAt(pt2);
          if (chr == ')') pcnt++;
          else if (chr == '(') pcnt--;
          if (pcnt == 0) break;
        }
        if (pt2 < 0) abort();
        data.strCross = cleanCross(field.substring(pt2+1, pt1).trim());
        field = field.substring(0,pt2).trim();
        flags |= FLAG_NO_CITY;
      } else {
        String[] parts = MSPACE_PTN.split(field);
        if (parts.length == 4) {
          data.strCall = parts[0];
          parseAddress(parts[1], data);
          data.strCross = cleanCross(parts[2]);
          data.strCity = convertCodes(parts[3], CITY_CODES);
          return;
        }
        else flags |= FLAG_PAD_FIELD;
      }
      field = DIR_OF_SLASH_PTN.matcher(field).replaceAll("$1 ").trim();
      parseAddress(StartType.START_CALL, flags, field, data);
      if ((flags & FLAG_PAD_FIELD) != 0) data.strCross = cleanCross(getPadField());
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL ADDR APT X CITY";
    }
  }
  
  private static final Pattern CROSS_PFX_PTN = Pattern.compile("btwn |low xst:");
  private class MyCross2Field extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      Matcher match = CROSS_PFX_PTN.matcher(field);
      if (!match.lookingAt()) abort();
      field = field.substring(match.end()).trim();
      super.parse(field, data);
    }
  }
  
  private static final Pattern PLACE_CITY_PTN = Pattern.compile("(.*), *[A-Z]{3}");
  private class MyPlace2Field extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PLACE_CITY_PTN.matcher(field);
      if (match.matches()) field = match.group(1).trim();
      super.parse(field, data);
    }
  }
  
  private class MyName2Field extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = cleanWirelessCarrier(field, true);
      if (checkPlace(field)) {
        data.strPlace = append(data.strPlace, " - ", field);
      } else {
        data.strName = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "X NAME";
    }
  }
  
  private String expandCall(String call) {
    SortedSet<String> tail =  CALL_SET.tailSet(call);
    String result = null;
    for (String key : tail) {
      if (key.equals(call)) return call;
      if (key.startsWith(call)) {
        if (result == null) result = key;
        else return call;
      } else break;
    }
    return result == null ? call : result;
  }

  private static final String[] CALL_ARRAY = new String[]{
      "AB PAIN W/FAINTG",
      "ABANDON VEHICLE",
      "ABANDONED VEHICLE",
      "ABDOMINAL PAIN",
      "ABDOMINAL PAIN ALPHA",
      "ABDOMINAL PAIN BRAVO",
      "ABDOMINAL PAIN CHARLIE",
      "ABDOMINAL PAIN DELTA",
      "ABDOMINAL PAIN ECHO",
      "ABDOMINAL-ALPHA RESPONSE",
      "ABDOMINAL-BRAVO RESPONSE",
      "ABDOMINAL-CHARLIE RESPONSE",
      "ABDOMINAL-DELTA RESPONSE",
      "ABDOMINAL-ECHO RESPONSE",
      "ABDOMINAL PAIN C1",
      "ABDOMINL PAIN C1",
      "AIR ALERT2:TASK",
      "AIR ALERT3:BOX",
      "ALARM SILENT",
      "ALARM,AUDIBLE",
      "ALARM,OTHER",
      "ALARM,PANIC",
      "ALARM,SILENT",
      "ALLERGIC REACTIO",
      "ALLERGIC REACTION",
      "ALLERGIES-ALPHA RESPONSE",
      "ALLERGIES-BRAVO RESPONSE",
      "ALLERGIES-CHARLIE RESPONSE",
      "ALLERGIES-DELTA RESPONSE",
      "ALLERGIES-ECHO RESPONSE",
      "ALLERGIES/ENVENOMATIONS CHARLI",
      "ALLERGY PROBLEM",
      "ALLERGY/ALT LOC",
      "AMR TRANSPORT",
      "ANIMAL BITE C1",
      "ANIMAL BITE/ATTA",
      "ANIMAL BITES/ATTACKS ALPHA",
      "ANIMAL BITES/ATTACKS BRAVO",
      "ANIMAL BITES/ATTACKS CHARLIE",
      "ANIMAL BITES/ATTACKS DELTA",
      "ANIMAL BITES/ATTACKS ECHO",
      "ANIMAL COMPLAINT",
      "ARREST/CUSTODY",
      "ASSAULT",
      "ASSAULT/SEX ASLT/STUN GUN ALPHA",
      "ASSAULT/SEX ASLT/STUN GUN BRAVO",
      "ASSAULT/SEX ASLT/STUN GUN CHARLIE",
      "ASSAULT/SEX ASLT/STUN GUN DELTA",
      "ASSAULT/SEX ASLT/STUN GUN ECHO",
      "ASSAUTL/SEX ASLT/STUN GUN ALPHA",
      "ASSAUTL/SEX ASLT/STUN GUN BRAVO",
      "ASSAUTL/SEX ASLT/STUN GUN CHARLIE",
      "ASSAUTL/SEX ASLT/STUN GUN DELTA",
      "ASSAUTL/SEX ASLT/STUN GUN ECHO",
      "ASSIST FIRE",
      "ASSIST OUTSIDE AGENCY",
      "ASSIST PERSON",
      "ASSIST PUBLIC",
      "ASSLT/RAPE/STAB",
      "ASTHMA PROBLEM",
      "ATTEMPT WARRANT",
      "ALARM AUDIBLE",
      "BACK PAIN",
      "BACK PAIN C1",
      "BACK PAIN (NON-TRAUMA) ALPHA",
      "BACK PAIN (NON-TRAUMA) BRAVO",
      "BACK PAIN (NON-TRAUMA) CHARLIE",
      "BACK PAIN (NON-TRAUMA) DELTA",
      "BACK PAIN (NON-TRAUMA) ECHO",
      "BACK PAIN-ALPHA RESPONSE",
      "BACK PAIN-BRAVO RESPONSE",
      "BACK PAIN-CHARLIE RESPONSE",
      "BACK PAIN-DELTA RESPONSE",
      "BACK PAIN-ECHO RESPONSE",
      "BARK DUST FIRE",
      "BARKDUST FIRE",
      "BARN FIRE",
      "BEHAVIOR/PSYCH",
      "BEHAVIOR/PSYC C1",
      "BEHAVIORAL HEALTH",
      "BLEEDG/AB BREATH",
      "BLEEDING PROBLEM",
      "BLEEDING/MINOR",
      "BLEEDING/POS DGR",
      "BLEEDING/SERIOUS",
      "BLEEDING/THINNER",
      "BP ABNORMAILITY",
      "BREATH PROB C1",
      "BREATHING PROB",
      "BREATHING PROB.",
      "BREATHING PROBLEM",
      "BREATHING PROBLEMS ALPHA",
      "BREATHING PROBLEMS BRAVO",
      "BREATHING PROBLEMS CHARLIE",
      "BREATHING PROBLEMS DELTA",
      "BREATHING PROBLEMS ECHO",
      "BREATHING PROBLEMS DELTA",
      "BREATHING-ALPHA RESPONSE",
      "BREATHING-BRAVO RESPONSE",
      "BREATHING-CHARLIE RESPONSE",
      "BREATHING-DELTA RESPONSE",
      "BREATHING-ECHO RESPONSE",
      "BRUSH FIRE",
      "BURGLARY,RESIDENTIAL",
      "BURN COMPLAINT",
      "BURNS/EXPLOSIONS ALPHA",
      "BURNS/EXPLOSIONS BRAVO",
      "BURNS/EXPLOSIONS CHARLIE",
      "BURNS/EXPLOSIONS DELTA",
      "BURNS/EXPLOSIONS ECHO",
      "CAD TO CAD",
      "CAR FIRE",
      "CARBON MONOXIDE POISON",
      "CARBON MONOXIDE/INH/HAZ ALPHA",
      "CARBON MONOXIDE/INH/HAZ BRAVO",
      "CARBON MONOXIDE/INH/HAZ CHARLIE",
      "CARBON MONOXIDE/INH/HAZ DELTA",
      "CARBON MONOXIDE/INH/HAZ ECHO",
      "CARD/RESP ARREST",
      "CARDIAC ARREST",
      "CARDIAC/RESP ARREST ALPHA",
      "CARDIAC/RESP ARREST BRAVO",
      "CARDIAC/RESP ARREST CHARLIE",
      "CARDIAC/RESP ARREST DELTA",
      "CARDIAC/RESP ARREST ECHO",
      "CHEST PAIN",
      "CHEST PAIN ALPHA",
      "CHEST PAIN BRAVO",
      "CHEST PAIN CHARLIE",
      "CHEST PAIN DELTA",
      "CHEST PAIN-ALPHA RESPONSE",
      "CHEST PAIN-BRAVO RESPONSE",
      "CHEST PAIN-CHARLIE RESPONSE",
      "CHEST PAIN-DELTA RESPONSE",
      "CHEST PAIN-ECHO RESPONSE",
      "CHEST PAIN/HEART",
      "CHEST/>35 YEARS",
      "CHEST/ABN BREATH",
      "CHEST/CARDIAC HX",
      "CHEST/CLAMMY",
      "CHEST/COLOR CHG",
      "CHEST/DIF BREATH",
      "CHEST/NOT ALERT",
      "CHIMNEY FIRE",
      "CHK EXTINGUISHED",
      "CHOKE/NPART OBST",
      "CODE 1 MEDICAL REQUEST",
      "CODE 2 MEDICAL REQUEST",
      "CODE 3 MEDICAL REQUEST",
      "CHEST PAIN CHARLIE",
      "CHEST/CLAMMY",
      "CHOKING",
      "CHOKING ALPHA",
      "CHOKING BRAVO",
      "CHOKING CHARLIE",
      "CHOKING DELTA",
      "CHOKING ECHO",
      "CHOKING-ALPHA RESPONSE",
      "CHOKING-BRAVO RESPONSE",
      "CHOKING-CHARLIE RESPONSE",
      "CHOKING-DELTA RESPONSE",
      "CHOKING-ECHO RESPONSE",
      "CIVIL",
      "CODE 1 MEDICAL POLICE REQUEST",
      "COMMERCIAL FIRE AL",
      "COMMERCIAL FIRE ALAR",
      "COMMERCIAL FIRE ALARM",
      "COMMERCIAL F",
      "COMMERCIAL FIRE",
      "CONVULSION/SEIZU",
      "CONVULSIONS/SEIZURES ALPHA",
      "CONVULSIONS/SEIZURES BRAVO",
      "CONVULSIONS/SEIZURES CHARLIE",
      "CONVULSIONS/SEIZURES DELTA",
      "CONVULSIONS/SEIZURES ECHO",
      "CRIMINAL MISCHIEF",
      "DANGEROUS BLEED",
      "DEATH INVESTIGATION",
      "DETECTOR PROBLEM",
      "DHS REFERRAL",
      "DIABETIC PROBLEM",
      "DIABETIC PROBLEMS",
      "DIABETIC PROBLEMS ALPHA",
      "DIABETIC PROBLEMS BRAVO",
      "DIABETIC PROBLEMS CHARLIE",
      "DIABETIC PROBLEMS DELTA",
      "DIABETIC PROBLEMS ECHO",
      "DIABETIC",
      "DIABETIC C1",
      "DISTURBANCE",
      "DIZZY/VERTIGO",
      "DOMESTIC DISTURBANCE",
      "DROWNING/DIVING/SCUBA",
      "DUII",
      "DUMPSTER FIRE",
      "ELDER ABUSE OR NEGLECT",
      "ELECTRICAL FIRE",
      "EXTRA PATROL",
      "EXTRA PATROL COMPLETED",
      "EXTRA PATROL REQUEST",
      "F/A, ALL OTHER",
      "FAINT/AB BREATH",
      "FAINT/CARDIAC HX",
      "FAINTING",
      "FAINTING/ALT LOC",
      "FALL C1",
      "FALL",
      "FALL-ALPHA RESPONSE",
      "FALL-BRAVO RESPONSE",
      "FALL-CHARLIE RESPONSE",
      "FALL-DELTA RESPONSE",
      "FALL-ECHO RESPONSE",
      "FALLS ALPHA",
      "FALLS BRAVO",
      "FALLS CHARLIE",
      "FALLS DELTA",
      "FALLS ECHO",
      "FEVER/CHILLS",
      "FIRE ALARM, COMM",
      "FIRE ALARM, RESD",
      "FIRE ALARM,RESID",
      "FIRE INFORMATION",
      "FIRE, SINGLE ENGINE",
      "FIRE/STRUCTURE",
      "FIREWORKS",
      "FIX-IT TICKET",
      "FOLLOW UP",
      "FOLLOW UP CONTACT",
      "FRAUD",
      "FRAUD COLD",
      "GENERAL WEAKNESS",
      "GRASS FIRE",
      "HANGING",
      "HARASSMENT",
      "HAZARD",
      "HAZARDOUS MAT",
      "HAZMAT INCIDENT",
      "HEADACHE C1",
      "HEADACHE",
      "HEADACHE CHARLIE",
      "HEADACHE-CHARLIE RESPONSE",
      "HEART PROBLEM-ALPHA RESPONSE",
      "HEART PROBLEM-BRAVO RESPONSE",
      "HEART PROBLEM-CHARLIE RESPONSE",
      "HEART PROBLEM-DELTA RESPONSE",
      "HEART PROBLEM-ECHO RESPONSE",
      "HEART PROBLEMS",
      "HEART PROBLEMS/AICD ALPHA",
      "HEART PROBLEMS/AICD BRAVO",
      "HEART PROBLEMS/AICD CHARLIE",
      "HEART PROBLEMS/AICD DELTA",
      "HEART PROBLEMS/AICD ECHO",
      "HEART/ABN BREATH",
      "HEART/CARDIAC HX",
      "HEART/CLAMMY",
      "HEART/DIF BREATH",
      "HEAT EXPOSURE",
      "HEAT/COLD EXPOSU",
      "HEAT/COLD EXPOSURE ALPHA",
      "HEMORRHAGE/LACERATION ALPHA",
      "HEMORRHAGE/LACERATION BRAVO",
      "HEMORRHAGE/LACERATION CHARLIE",
      "HEMORRHAGE/LACERATION DELTA",
      "HEMORRHAGE/LACERATION ECHO",
      "HIT / RUN NON-INJURY",
      "HIT AND RUN INJ",
      "HIT AND RUN,NON INJURY",
      "HOSP TXPORT C1",
      "ILLEGAL BURN",
      "IMMINENT BIRTH",
      "IMPOUNDED VEHICLE",
      "INCOMPLETE 911",
      "INDUSTRIAL ACCIDENT",
      "INFORMATION",
      "INFORMATION FIRE",
      "INVALID ASSIST",
      "JUVENILE ABUSE OR NEGLECT",
      "JUVENILE CUSTODY PROBLEM",
      "JUVENILE PROBLEM",
      "JUVENILE RUNAWAY",
      "LANDING ZONE",
      "LIFT ASSIST",
      "LOCKOUT",
      "MARINE RESCUE",
      "MARINE RESCUE 1",
      "MARINE RESCUE 2",
      "MEDICAL ALARM",
      "MEDICAL ALARM-1",
      "MEDICAL TRANSPORT CODE",
      "MEDICAL-DETAILS TO FOLLOW",
      "MENTAL",
      "MENTAL/PSYCH",
      "MINOR IN POSSESSION",
      "MISC FIRE",
      "MISC NO FIRE",
      "MISC NON FIRE",
      "MISCELLANEOUS",
      "MISCELLANEOUS - FIRE",
      "MISSING PERSON",
      "MISSING PERSON; LOCATED",
      "MOTORIST ASSIST",
      "MOVEUP",
      "MR1",
      "MR2",
      "MUTUAL AID",
      "MVA-INJURY ACCID",
      "MVA-UKN INJURY",
      "MVA-UNK INJURY",
      "MVA/INJURY ACCDT",
      "MVA/UNKNOWN INJ",
      "NATURAL GAS LEAK",
      "NOISE COMPLAINT",
      "OBVIOUS DEATH",
      "ODOR INVESTIGATE",
      "ODOR INVESTIGATION",
      "OPEN DOOR/WINDOW",
      "ORDINANCE VIOLATION",
      "OTHER PAIN",
      "OUT WITH SUSP VEHICLE",
      "OVERDOSE/POISON",
      "OVERDOSE/POISONING ALPHA",
      "OVERDOSE/POISONING BRAVO",
      "OVERDOSE/POISONING CHARLIE",
      "OVERDOSE/POISONING DELTA",
      "OVERDOSE/POISONING ECHO",
      "PANDEMIC/EPIDEMIC/OUTBREAK ALP",
      "PARKING COMPLAINT",
      "POLE FIRE",
      "PRE NOTIFICATION",
      "PREG/CHILDBIRTH/MISCAR",
      "PREGNANCY/BIRTH",
      "PROPERTY INVESTIGATION",
      "PROPERTY LST/FND",
      "PROWLER",
      "PSYCH/AB BEH/SUICIDE ATT ALPH",
      "PSYCH/AB BEH/SUICIDE ATT BRAVO",
      "PSYCH/AB BEH/SUICIDE ATT CHARLIE",
      "PSYCH/AB BEH/SUICIDE ATT DELTA",
      "PSYCH/AB BEH/SUICIDE ATT ECHO",
      "PSYCH/ABNORM/SUA",
      "PSYCHIATRIC",
      "PUBLIC ASSIST",
      "RECALL",
      "REQUEST FOR K-9 UNIT",
      "RESIDENTIAL ALARM",
      "RESIDENTIAL FIRE ALARM",
      "RESIDENTIAL FIRE",
      "RFIRE",
      "ROBBERY,ARMED",
      "RUNAWAY JUVENILE",
      "SEIZE/CONTINUOUS",
      "SEIZE/NO TRAIGE",
      "SEIZE/UNK BREATH",
      "SEIZURE-CHARLIE ALPHA",
      "SEIZURE-CHARLIE BRAVO",
      "SEIZURE-CHARLIE RESPONSE",
      "SEIZURE-DELTA RESPONSE",
      "SEIZURE-ECHO RESPONSE",
      "SEIZURE/UNK STAT",
      "SEIZURES",
      "SEND MEDICAL CODE",
      "SEND MEDICAL CODE 3",
      "SHOOTING",
      "SICK PERSON",
      "SICK PERSON ALPHA",
      "SICK PERSON BRAVO",
      "SICK PERSON CHARLIE",
      "SICK PERSON DELTA",
      "SICK PERSON ECHO",
      "SICK PERSON-ALPHA RESP",
      "SICK PERSON-BRAVO RESP",
      "SICK PERSON-CHARLIE RESPONSE",
      "SICK PERSON-DELTA RESPONSE",
      "SICK PERSON-ECHO RESPONSE",
      "SICK PERSON C1",
      "SICK PERSON/UNKO",
      "SMOKE INVESTIGAT",
      "SMOKE SMELL",
      "SPILL, FUEL/UNK",
      "STOLEN VEHICLE",
      "STROKE",
      "STROKE (CVA)",
      "STROKE-CHARLIE RESPONSE",
      "STROKE/TIA ALPHA",
      "STROKE/TIA BRAVO",
      "STROKE/TIA CHARLIE",
      "STROKE/TIA DELTA",
      "STROKE/TIA ECHO",
      "SUA",
      "SUBJECT STOP",
      "SUICIDE ATTEMPT",
      "SUICIDE THREAT",
      "SUSPICIOUS CIRCUMSTANCES",
      "SUSPICIOUS PERSON",
      "SUSPICIOUS VEHICLE",
      "TAI",
      "TAI-HIGH MECHANI",
      "TAU",
      "TASK FORCE",
      "TEST FIRE",
      "TEST MEDICAL",
      "THEFT",
      "THEFT COLD",
      "THEFT, JUST OCCURRED",
      "TOXIC EXPOSURE",
      "TRAFFIC ACCIDENT",
      "TRAFFIC ACCIDENT INJURY",
      "TRAFFIC ACCIDENT UNK INJ",
      "TRAFFIC ACCIDENT, INJURY",
      "TRAFFIC ACCIDENT,INJ",
      "TRAFFIC ACCIDENT,NON-INJURY",
      "TRAFFIC ACCIDENT,UNK INJURY",
      "TRAFFIC ACCIDENT, UNK INJURY",
      "TRAFFIC COMPLAINT",
      "TRAFFIC STOP",
      "TRAFFIC/TRANSP INC ALPHA",
      "TRAFFIC/TRANSP INC BRAVO",
      "TRAFFIC/TRANSP INC CHARLIE",
      "TRAFFIC/TRANSP INC DELTA",
      "TRAFFIC/TRANSP INC ECHO",
      "TRANSFER/INTERFA",
      "TRAUMA C1",
      "TRAUMA",
      "TRAUMATIC INJURIES ALPHA",
      "TRAUMATIC INJURIES BRAVO",
      "TRAUMATIC INJURIES CHARLIE",
      "TRAUMATIC INJURIES DELTA",
      "TRAUMATIC INJURIES ECHO",
      "TRAUMATIC INJURIES-ALPHA",
      "TRAUMATIC INJURIES-BRAVO",
      "TRAUMATIC INJURIES-CHARLIE",
      "TRAUMATIC INJURIES-DELTA",
      "TRAUMATIC INJURIES-ECHO",
      "TRAUMATIC INJURY",
      "TREE FIRE",
      "TRF ACC NON-INJ",
      "TRF ACC, INJURY",
      "TRF ACC, NON-INJ",
      "TRF ACC, UNK INJ",
      "TRUCK FIRE",
      "UNCON/FAINTING",
      "UNCONCIOUS",
      "UNCONCIOUS/FAINT",
      "UNCONSCIOUS/FAINTING",
      "UNCONSCIOUS/FAINTING ALPHA",
      "UNCONSCIOUS/FAINTING BRAVO",
      "UNCONSCIOUS/FAINTING CHARLIE",
      "UNCONSCIOUS/FAINTING DELTA",
      "UNCONSCIOUS/FAINTING ECHO",
      "UNCONS/UNRESPONS",
      "UNCONSC/FAINTING",
      "UNCONSCIO/AGONAL",
      "UNCONSCIOUS/FAINTING-ALPHA",
      "UNCONSCIOUS/FAINTING-BRAVO",
      "UNCONSCIOUS/FAINTING-CHARLIE",
      "UNCONSCIOUS/FAINTING-DELTA",
      "UNCONSCIOUS/FAINTING-ECHO",
      "UNK PROB/MN DOWN",
      "UNKNOWN PROBLEM ALPHA",
      "UNKNOWN PROBLEM BRAVO",
      "UNKNOWN PROBLEM CHARLIE",
      "UNKNOWN PROBLEM DELTA",
      "UNKNOWN PROBLEM ECHO",
      "UNKNOWN PROBLEM-ALPHA RESP",
      "UNKNOWN PROBLEM-BRAVO RESP",
      "UNKNOWN PROBLEM-CHARLIE RESP",
      "UNKNOWN PROBLEM-DELTA RESP",
      "UNKNOWN PROBLEM-ECHO RESP",
      "UNKNOWN TYP FIRE",
      "UNWANTED",
      "UNWELL/ILL",
      "VEHICLE FIRE",
      "VEHICLE RELEASE",
      "VIOLATION OF RESTRAINING ORDER",
      "VOMITING",
      "WARRANT SERVICE",
      "WATER PROBLEM",
      "WELFARE CHECK",
      "WIRES DOWN"
  }; 
  
  private final static CodeSet CALL_LIST = new CodeSet(CALL_ARRAY);
  private final static TreeSet<String> CALL_SET = new TreeSet<String>(Arrays.asList(CALL_ARRAY));
  
  private static String[] MWORD_STREET_LIST = new String[]{
      "ALDER CREST",
      "ARCHERY SUMMIT",
      "ASH CREEK",
      "BAKER TRAIL",
      "BARLOW TRAIL",
      "BAY MEADOWS",
      "BEACH PLUM",
      "BLUE HERON",
      "BOONES FERRY",
      "BREYMAN ORCHARDS",
      "BRIGHTWOOD BRIDGE",
      "BUD SMITH",
      "BULL MOUNTAIN",
      "BUXTON LOOKOUT",
      "CARMEN HEIGHTS",
      "CASCADIA VILLAGE",
      "CEDAR CANYON",
      "CEDAR FALLS",
      "CLAPSHAW HILL",
      "COLLINS LAKE",
      "COPPER BEECH",
      "CORNELIUS PASS",
      "COUNTRY CLUB",
      "CROSS CREEK",
      "DICKEY PRAIRIE",
      "EAGLE CREEK",
      "EAGLE CREST",
      "EAGLES VIEW",
      "EL RANCHO",
      "ELAM YOUNG",
      "ELK PRAIRIE",
      "FALCON CREST",
      "FANNO CREEK",
      "FAR VISTA",
      "FERN HILL",
      "FINNIGAN HILL",
      "FLYING FEATHER",
      "FOX FARM",
      "FULQUARTZ LANDING",
      "GALES CREEK",
      "GARDEN MEADOW",
      "GLEN ECHO",
      "GOVERNMENT CAMP",
      "GRAHAMS FERRY",
      "GREEN MOUNTAIN",
      "HAZEL DELL",
      "HERB HILL",
      "HIDDEN SPRINGS",
      "HOLLY RIDGE",
      "HOLY NAMES",
      "HOSKINS HILL",
      "HUNT CLUB",
      "INDIAN SPRINGS",
      "IRON MOUNTAIN",
      "JACKSON SCHOOL",
      "JEREMY LOVELESS",
      "JOHN LEE",
      "KANSAS CITY",
      "KATIE ROSE",
      "KELLOGG CREEK",
      "KIWANIS CAMP",
      "KNOX RIDGE",
      "KRUSE OAKS",
      "KRUSE WAY",
      "LADD HILL",
      "LAKE GROVE",
      "LAKE SHORE",
      "LILAC HILL",
      "LOLO PASS",
      "LOST PARK",
      "MCKENZIE VALLEY",
      "METZLER PARK",
      "MINTER BRIDGE",
      "MONTE VERDI",
      "MOUNTAIN TOP",
      "NORTH DAKOTA",
      "NORTH SHORE",
      "NORTH VALLEY",
      "ORENCO GARDENS",
      "ORENCO STATION",
      "PACIFIC HWY FRONTAGE",
      "PLEASANT HILL",
      "POWELL HILL",
      "QUEEN ELIZABETH",
      "RED HEREFORD",
      "REGAN HILL",
      "RIBBON RIDGE",
      "RIDGE LAKE",
      "RIDGE POINTE",
      "RISING STAR",
      "RIVER BEND",
      "ROYAL VILLA",
      "SALMON RIVER",
      "SCHOLLS FERRY",
      "SCOGGINS VALLEY",
      "SNOWBERRY RIDGE",
      "SOUTH FORK GALES CREEK",
      "SOUTH HAMPTON",
      "SOUTH SHORE",
      "SPRING GARDEN",
      "STOREY BURN",
      "THE GREENS",
      "TUALATIN SHERWOOD",
      "TUALATIN VALLEY",
      "TUMALA MOUNTAIN",
      "TWIN FIR",
      "UNION HALL",
      "VALERIA VIEW",
      "VISION RIDGE",
      "WARDEN HILL",
      "WARM SPRINGS",
      "WEST A",
      "WEST POINT",
      "WEST SHORE",
      "WEST UNION",
      "WILD ROSE",
      "WILLAMETTE FALLS",
      "WILSON RIVER",
      "WILSON SCHOOL",
      "WIND SONG",
      "WORDEN HILL"
  };
  
  static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALO", "ALOHA",
      "AMI", "AMITY",
      "ARI", "ARIEL",
      "AUM", "AUMSVILLE",
      "AUR", "AURORA",
      "BAN", "BANKS",
      "BEA", "BEAVERCREEK",
      "BIN", "BINGEN",
      "BOR", "BORING",
      "BRI", "BRIGHTWOOD",
      "BRV", "BRIDAL VEIL",
      "BTN", "BEAVERTON",
      "BUX", "BUXTON",
      "CAN", "CANBY",
      "CAR", "CARLTON",
      "CAS", "CASCADE LOCKS",
      "CLA", "CLACKAMAS",
      "COL", "COLTON",
      "CON", "CORNELIUS",
      "COR", "CORBET",
      "DAL", "DALLESPORT",
      "DAM", "DAMASCUS",
      "DAY", "DAYTON",
      "DET", "DETROIT",
      "DUF", "DUFUR",
      "DUN", "DUNDEE",
      "EAG", "EAGLE CREEK",
      "EST", "ESTACADA",
      "FAI", "FAIRVIEW",
      "FOR", "FOREST GROVE",
      "GAL", "GALES CREEK",
      "GAS", "GASTON",
      "GAT", "GATES",
      "GER", "GERVAIS",
      "GLA", "GLADSTONE",
      "GOV", "GOVERNMENT CAMP",
      "GRE", "GRESHAM",
      "HAP", "HAPPY VALLEY",
      "HIL", "HILLSBORO",
      "HOO", "HOOD RIVER",
      "HUB", "HUBBARD",
      "IDA", "IDANHA",
      "IND", "INDEPENDENCE",
      "JEF", "JEFFERSON",
      "KEI", "KEIZER",
      "LAF", "LAFAYETTE",
      "LAK", "LAKE OSWEGO",
      "LYL", "LYLE",
      "LYO", "LYONS",
      "MAN", "MANNING",
      "MAR", "MARYLHURST",
      "MAU", "MAUPIN",
      "MCM", "MCMINNVILLE",
      "MCY", "MILL CITY",
      "MIL", "MILWAUKIE",
      "MOL", "MOLALLA",
      "MOS", "MOSIER",
      "MOU", "MOUNT ANGEL",
      "MTH", "MT HOOD PARKDALE",
      "MUL", "MULINO",
      "NEW", "NEWBERG",
      "NOR", "NORTH PLAINS",
      "ODE", "ODELL",
      "ORE", "OREGON CITY",
      "OC" , "OREGON CITY",
      "ORI", "ORIENT",
      "POR", "PORTLAND",
      "RHO", "RHODODENDRON",
      "RIC", "RICKREALL",
      "RID", "RIDGEFIELD",
      "SAL", "SALEM",
      "SAN", "SANDY",
      "SCA", "SCAPPOOSE",
      "SCO", "SCOTTS MILLS",
      "SDN", "SHERIDAN",
      "SEA", "SEASIDE",
      "SHE", "SHERWOOD",
      "SIL", "SILVERTON",
      "STA", "STAYTON",
      "STE", "STEVENSON",
      "STP", "SAINT PAUL",
      "SUB", "SUBLIMITY",
      "THE", "THE DALLES",
      "TIG", "TIGARD",
      "TIM", "TIMBER",
      "TRO", "TROUTDALE",
      "TUA", "TUALATIN",
      "TUR", "TURNER",
      "TYG", "TYGH VALLEY",
      "UND", "UNDERWOOD",
      "USF", "US FOREST SVC",
      "VAN", "VANCOUVER",
      "VER", "VERNONIA",
      "WAR", "WARM SPRINGS",
      "WEL", "WELCHES",
      "WES", "WEST LINN",
      "WHI", "WHITE SALMON",
      "WIL", "WILSONVILLE",
      "WOO", "WOODBURN",
      "YAM", "YAMHILL",
      "RVR", "RIVER FEATURES"
  });
}
