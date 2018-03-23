package net.anei.cadpage.parsers.OR;

import java.util.Properties;
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

  private static final Pattern MASTER_PTN2 = Pattern.compile("([A-Z0-9]+) +\\*[A-Z] +(\\d\\d:\\d\\d:\\d\\d) (.*?)  Dt: ([^ ]+)  Zn: (\\d+)  Gd: ([^ ]+) (.*) /");
  
  private static final Pattern MASTER_PTN3 = Pattern.compile("Call for (?:([-A-Z0-9]{2,5}) - )?(.*?)(?: +|(?<=[A-Z]))at (.*?) Units resp[. ]([A-Z0-9,]+) *(?:time: ?(\\d\\d:\\d\\d)(?:Call for)?|(\\[.*))");
  
  private static final Pattern MASTER_PTN4 = Pattern.compile("([A-Z]{2,4}\\d{4}-\\d{7}) call for (.*?)at (.*?) cross streets (.*?)\\bUnits resp\\. (\\S+) +\\[1\\] +(.*)");
  
  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ ,]*\\[\\d+\\][ ,]*");

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
    
    // Yet another alternate format
    match = MASTER_PTN2.matcher(body);
    if (match.matches()) {
      setFieldList("CALL TIME ADDR APT MAP X");
      data.strCall = match.group(1);
      data.strTime = match.group(2);
      parseAddress(match.group(3).trim(), data);
      data.strMap = append(append(match.group(4), "-", match.group(5)), "-", match.group(6));
      data.strCross = match.group(7);
      return true;
    }
    
    // They keep on coming
    match = MASTER_PTN3.matcher(body);
    if (match.matches()) {
      setFieldList("CODE CALL ADDR APT UNIT TIME INFO");
      data.strCode = getOptGroup(match.group(1));
      data.strCall = match.group(2).trim();
      parseAddress(match.group(3).trim(), data);
      data.strUnit = match.group(4).trim();
      data.strTime = getOptGroup(match.group(5));
      data.strSupp = getOptGroup(match.group(6));
      return true;
    }
    
    match = MASTER_PTN4.matcher(body);
    if (match.matches()) {
      setFieldList("ID CALL ADDR APT X UNIT INFO");
      data.strCallId = match.group(1);
      data.strCall = match.group(2).trim();
      parseAddress(match.group(3).trim(), data);
      data.strCross = match.group(4).trim();
      data.strUnit = match.group(5).trim();
      String info = match.group(6).trim();
      for (String part : INFO_BRK_PTN.split(info)) {
        data.strSupp = append(data.strSupp, "\n", part.trim());
      }
      return true;
    }

    version = "1";
    return super.parseMsg(body, data);
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

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([-A-Z0-9]{2,5}) - +(.*)");
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
      
      int flags = FLAG_START_FLD_REQ;
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
        data.strCross = field.substring(pt2+1, pt1).trim();
        field = field.substring(0,pt2).trim();
        flags |= FLAG_ANCHOR_END | FLAG_NO_CITY;
      } else {
        String[] parts = MSPACE_PTN.split(field);
        if (parts.length == 4) {
          data.strCall = parts[0];
          parseAddress(parts[1], data);
          data.strCross = parts[2];
          data.strCity = convertCodes(parts[3], CITY_CODES);
          return;
        }
        else flags |= FLAG_PAD_FIELD;
      }
      field = DIR_OF_SLASH_PTN.matcher(field).replaceAll("$1 ").trim();
      parseAddress(StartType.START_CALL, flags, field, data);
      if ((flags & FLAG_PAD_FIELD) != 0) data.strCross = getPadField();
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
  
  private final static CodeSet CALL_LIST = new CodeSet(
      "AB PAIN W/FAINTG",
      "ABANDONED VEHICLE",
      "ABDOMINAL PAIN",
      "ABDOMINAL PAIN ALPHA",
      "ABDOMINAL PAIN BRAVO",
      "ABDOMINAL PAIN CHARLIE",
      "ABDOMINAL PAIN DELTA",
      "ABDOMINAL-CHARLIE RESPONSE",
      "ABDOMINAL-DELTA RESPONSE",
      "ABDOMINL PAIN C1",
      "AIR ALERT2:TASK",
      "AIR ALERT3:BOX",
      "ALARM,AUDIBLE",
      "ALARM,OTHER",
      "ALARM,PANIC",
      "ALARM,SILENT",
      "ALLERGIC REACTIO",
      "ALLERGIES-ALPHA RESPONSE",
      "ALLERGIES-BRAVO RESPONSE",
      "ALLERGIES-CHARLIE RESPONSE",
      "ALLERGIES-DELTA RESPONSE",
      "ALLERGIES/ENVENOMATIONS CHARLI",
      "ALLERGY PROBLEM",
      "ALLERGY/ALT LOC",
      "AMR TRANSPORT",
      "ANIMAL BITE C1",
      "ANIMAL BITE/ATTA",
      "ANIMAL COMPLAINT",
      "ARREST/CUSTODY",
      "ASSAULT",
      "ASSAULT/SEX ASLT/STUN GUN BRAV",
      "ASSIST OUTSIDE AGENCY",
      "ASSIST PUBLIC",
      "ASSLT/RAPE/STAB",
      "ASTHMA PROBLEM",
      "ATTEMPT WARRANT",
      "BACK PAIN",
      "BACK PAIN C1",
      "BACK PAIN (NON-TRAUMA) ALPHA",
      "BACK PAIN-ALPHA RESPONSE",
      "BARKDUST FIRE",
      "BARN FIRE",
      "BEHAVIOR/PSYCH",
      "BEHAVIOR/PSYC C1",
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
      "BREATHING PROBLEMS ALPHA",
      "BREATHING PROBLEMS BETA",
      "BREATHING PROBLEMS CHARLIE",
      "BREATHING PROBLEMS DELTA",
      "BREATHING-CHARLIE RESPONSE",
      "BREATHING-DELTA RESPONSE",
      "BRUSH FIRE",
      "BURGLARY,RESIDENTIAL",
      "BURN COMPLAINT",
      "CAD TO CAD",
      "CAR FIRE",
      "CARBON MONOXIDE POISON",
      "CARBON MONOXIDE/INH/HAZ ALPHA",
      "CARBON MONOXIDE/INH/HAZ BRAVO",
      "CARBON MONOXIDE/INH/HAZ CHARLIE",
      "CARBON MONOXIDE/INH/HAZ DELTA",
      "CARD/RESP ARREST",
      "CARDIAC ARREST",
      "CARDIAC/RESP ARREST ECHO",
      "CHEST PAIN",
      "CHEST PAIN ALPHA",
      "CHEST PAIN BRAVO",
      "CHEST PAIN CHARLIE",
      "CHEST PAIN DELTA",
      "CHEST PAIN-CHARLIE RESPONSE",
      "CHEST PAIN-DELTA RESPONSE",
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
      "CHOKING-DELTA RESPONSE",
      "CIVIL",
      "CODE 1 MEDICAL POLICE REQUEST",
      "COMMERCIAL FIRE ALAR",
      "COMMERCIAL FIRE ALARM",
      "COMMERCIAL F",
      "COMMERCIAL FIRE",
      "CONVULSION/SEIZU",
      "CONVULSIONS/SEIZURES ALPHA",
      "CONVULSIONS/SEIZURES BRAVO",
      "CONVULSIONS/SEIZURES CHARLIE",
      "CONVULSIONS/SEIZURES DELTA",
      "CRIMINAL MISCHIEF",
      "DANGEROUS BLEED",
      "DEATH INVESTIGATION",
      "DETECTOR PROBLEM",
      "DHS REFERRAL",
      "DIABETIC PROBLEM",
      "DIABETIC PROBLEMS ALPHA",
      "DIABETIC PROBLEMS BRAVO",
      "DIABETIC PROBLEMS CHARLIE",
      "DIABETIC PROBLEMS DELTA",
      "DIABETIC",
      "DIABETIC C1",
      "DISTURBANCE",
      "DIZZY/VERTIGO",
      "DOMESTIC DISTURBANCE",
      "DUII",
      "DUMPSTER FIRE",
      "ELDER ABUSE OR NEGLECT",
      "ELECTRICAL FIRE",
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
      "FALL-DELTA RESPONSE",
      "FALLS ALPHA",
      "FALLS BRAVO",
      "FALLS CHARLIE",
      "FALLS DELTA",
      "FEVER/CHILLS",
      "FIRE ALARM, COMM",
      "FIRE ALARM, RESD",
      "FIRE ALARM,RESID",
      "FIRE INFORMATION",
      "FIRE, SINGLE ENGINE",
      "FIRE/STRUCTURE",
      "FIREWORKS",
      "FIX-IT TICKET",
      "FOLLOW UP CONTACT",
      "FRAUD",
      "GENERAL WEAKNESS",
      "GRASS FIRE",
      "HANGING",
      "HARASSMENT",
      "HAZARD",
      "HAZARDOUS MAT",
      "HAZMAT",
      "HAZMAT INCI",
      "HEADACHE C1",
      "HEADACHE",
      "HEADACHE-CHARLIE RESPONSE",
      "HEART PROBLEM-CHARLIE RESPONSE",
      "HEART PROBLEM-DELTA RESPONSE",
      "HEART PROBLEMS",
      "HEART PROBLEMS/AICD ALPHA",
      "HEART PROBLEMS/AICD BRAVO",
      "HEART PROBLEMS/AICD CHARLIE",
      "HEART PROBLEMS/AICD DELTA",
      "HEART/ABN BREATH",
      "HEART/CARDIAC HX",
      "HEART/CLAMMY",
      "HEART/DIF BREATH",
      "HEAT EXPOSURE",
      "HEAT/COLD EXPOSU",
      "HEMORRHAGE/LACERATION ALPHA",
      "HEMORRHAGE/LACERATION BETA",
      "HEMORRHAGE/LACERATION CHARLIE",
      "HEMORRHAGE/LACERATION DELTA",
      "HIT AND RUN INJ",
      "HIT AND RUN,NON INJURY",
      "HOSP TXPORT C1",
      "ILLEGAL BURN",
      "IMMINENT BIRTH",
      "IMPOUNDED VEHICLE",
      "INCOMPLETE 911",
      "INFORMATION",
      "INFORMATION F",
      "INVALID ASSIST",
      "JUVENILE ABUSE OR NEGLECT",
      "JUVENILE CUSTODY PROBLEM",
      "JUVENILE PROBLEM",
      "LANDING ZONE",
      "LIFT ASSIST",
      "LOCKOUT",
      "MARINE RESCUE",
      "MARINE RESCUE 1",
      "MARINE RESCUE 2",
      "MEDICAL ALARM",
      "MEDICAL TRANSPORT CODE",
      "MEDICAL-DETAILS TO FOLLOW",
      "MENTAL",
      "MENTAL/PSYCH",
      "MISC FIRE",
      "MISC NO FIRE",
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
      "OPEN DOOR/WINDOW",
      "ORDINANCE VIOLATION",
      "OTHER PAIN",
      "OVERDOSE/POISON",
      "PANDEMIC/EPIDEMIC/OUTBREAK ALP",
      "PARKING COMPLAINT",
      "POLE FIRE",
      "PRE NOTIFICATION",
      "PREGNANCY/BIRTH",
      "PROPERTY INVESTIGATION",
      "PROWLER",
      "PSYCH/AB BEH/SUICIDE ATT ALPH",
      "PSYCH/AB BEH/SUICIDE ATT BRAVO",
      "PSYCH/AB BEH/SUICIDE ATT CHARLIE",
      "PSYCH/AB BEH/SUICIDE ATT DELTA",
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
      "SEIZURE-CHARLIE RESPONSE",
      "SEIZURE-DELTA RESPONSE",
      "SEIZURE-ECHO RESPONSE",
      "SEIZURE/UNK STAT",
      "SEIZURES",
      "SHOOTING",
      "SICK PERSON",
      "SICK PERSON ALPHA",
      "SICK PERSON BRAVO",
      "SICK PERSON CHARLIE",
      "SICK PERSON DELTA",
      "SICK PERSON-ALPHA RESP",
      "SICK PERSON-BRAVO RESP",
      "SICK PERSON-CHARLIE RESPONSE",
      "SICK PERSON-DELTA RESPONSE",
      "SICK PERSON C1",
      "SICK PERSON/UNKO",
      "SMOKE INVESTIGAT",
      "SMOKE SMELL",
      "SPILL, FUEL/UNK",
      "STROKE",
      "STROKE (CVA)",
      "STROKE-CHARLIE RESPONSE",
      "SUA",
      "SUBJECT STOP",
      "SUICIDE ATTEMPT",
      "SUICIDE THREAT",
      "SUSPICIOUS CIRCUMSTANCES",
      "SUSPICIOUS PERSON",
      "SUSPICIOUS VEHICLE GONE",
      "SUSPICIOUS VEHICLE",
      "TAI",
      "TAI-HIGH MECHANI",
      "TAU",
      "TASK FORCE",
      "TEST FIRE",
      "TEST MEDICAL",
      "THEFT",
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
      "TRAFFIC COMPLAINT",
      "TRAFFIC STOP",
      "TRAFFIC STOP",
      "TRANSFER/INTERFA",
      "TRAUMA C1",
      "TRAUMA",
      "TRAUMATIC INJURIES ALPHA",
      "TRAUMATIC INJURIES BRAVO",
      "TRAUMATIC INJURIES CHARLIE",
      "TRAUMATIC INJURIES DELTA",
      "TRAUMATIC INJURIES-BRAVO",
      "TRAUMATIC INJURIES-DELTA",
      "TRAUMATIC INJURY",
      "TREE FIRE",
      "TRF ACC, INJURY",
      "TRF ACC, NON-INJ",
      "TRF ACC, UNK INJ",
      "TRUCK FIRE",
      "UNCON/FAINTING",
      "UNCONCIOUS",
      "UNCONCIOUS/FAINT",
      "UNCONSCIOUS/FAINTING",
      "UNCONSCIOUS/FAINTING DELTA",
      "UNCONS/UNRESPONS",
      "UNCONSC/FAINTING",
      "UNCONSCIO/AGONAL",
      "UNCONSCIOUS/FAINTING-CHARLIE",
      "UNCONSCIOUS/FAINTING-DELTA",
      "UNK PROB/MN DOWN",
      "UNKNOWN PROBLEM BRAVO",
      "UNKNOWN PROBLEM-BRAVO RESP",
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
  ); 
  
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
