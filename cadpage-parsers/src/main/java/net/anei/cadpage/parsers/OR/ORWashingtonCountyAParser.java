package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Washington County, OR
 * Also Clackamas County
 */
public class ORWashingtonCountyAParser extends ORWashingtonCountyBaseParser {
  
  private static final Pattern BAD_SUBJECT_PTN = Pattern.compile("PW(\\d+)|VisiCAD Email");
  
  private static final Pattern RECALL_PTN = Pattern.compile("(RECALL):(.*) C\\*\\d{5}(?:CANCEL|RECALL)?(.*)");
  private static final Pattern RECALL_CITY_PTN = Pattern.compile("(.*),([A-Z]{3}) (.*)");
  
  private static final Pattern MASTER_PTN1 = Pattern.compile("([A-Z0-9]+) - ([-A-Z0-9]{2,4}) \\((.*?)\\) (.*?)#([A-Z]{2}\\d+)\\b[, ]*(.*)");

  private static final Pattern MASTER_PTN2 = Pattern.compile("([A-Z0-9]+) +\\*[A-Z] +(\\d\\d:\\d\\d:\\d\\d) (.*?)  Dt: ([^ ]+)  Zn: (\\d+)  Gd: ([^ ]+) (.*) /");
  
  private String version;

  public ORWashingtonCountyAParser() {
    this("WASHINGTON COUNTY", "OR");
  }
  
  public ORWashingtonCountyAParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState,
          "( SELECT/1 ADDR1/SC! MAP:MAP UNIT:UNIT! | " +
            "ADDRCITY SRC! ( MAP:MAP X2 | ) DASH? PLACE2 NAME2! )");
    setupCallList(CALL_LIST);
  }
  
  @Override
  public String getAliasCode() {
    return "ORWashingtonCountyA";
  }
  
  @Override
  public String getFilter() {
    return "930010,777,888,wccca@wccca.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Reject anything for ORWashingtonCountyB or ORWashingtonCountyC
    if (BAD_SUBJECT_PTN.matcher(subject).matches()) return false;
    
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

  private static final Pattern DIR_OF_SLASH_PTN = Pattern.compile("\\b([NSEW] *OF) */ *");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String addr;
      String city = p.getLastOptional(')');
      if (city.length() > 0) {
        addr = p.get('(');
        data.strCross = p.get();
      } else {
        city = p.getLast(' ');
        addr = p.get();
      }
      addr = DIR_OF_SLASH_PTN.matcher(addr).replaceAll("$1 ").trim();
      parseAddress(StartType.START_CALL, FLAG_ANCHOR_END, addr, data);
      data.strCity = convertCodes(city, CITY_CODES);
      
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X CITY";
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
      "*E1",
      "*MW36",
      "*READ FIRST",
      "*TEST ONLY",
      "ABANDONED VEHICLE",
      "ABDOMINAL PAIN",
      "ABDOMINAL-CHARLIE RESPONSE",
      "ABDOMINAL-DELTA RESPONSE",
      "ABDOMINL PAIN C1",
      "AIR ALERT2:TASK",
      "ALARM,AUDIBLE",
      "ALARM,OTHER",
      "ALARM,PANIC",
      "ALLERGIC REACTIO",
      "ALLERGIES-CHARLIE RESPONSE",
      "ALLERGIES-DELTA RESPONSE",
      "AMR TRANSPORT",
      "ANIMAL COMPLAINT",
      "ARREST/CUSTODY",
      "ASSAULT",
      "ASSIST OUTSIDE AGENCY",
      "ASSIST PUBLIC",
      "ASSLT/RAPE/STAB",
      "ATTEMPT WARRANT",
      "BACK PAIN C1",
      "BACK PAIN",
      "BACK PAIN-ALPHA RESPONSE",
      "BARKDUST FIRE",
      "BARN FIRE",
      "BLEEDING PROBLEM",
      "BLEEDING/POS DGR",
      "BREATH PROB C1",
      "BREATHING PROB",
      "BREATHING PROB.",
      "BREATHING-CHARLIE RESPONSE",
      "BREATHING-DELTA RESPONSE",
      "BRUSH FIRE",
      "BURN COMPLAINT",
      "CAD TO CAD",
      "CAR FIRE",
      "CARBON MONOXIDE POISON",
      "CARD/RESP ARREST",
      "CARDIAC ARREST",
      "CHEST PAIN",
      "CHEST PAIN-CHARLIE RESPONSE",
      "CHEST PAIN-DELTA RESPONSE",
      "CHEST PAIN/HEART",
      "CHEST/>35 YEARS",
      "CHIMNEY FIRE",
      "CHK EXTINGUISHED",
      "CHOKING",
      "CHOKING-DELTA RESPONSE",
      "CIVIL",
      "CODE 1 MEDICAL POLICE REQUEST",
      "COMMERCIAL FIRE ALARM",
      "COMMERCIAL FIRE",
      "CONVULSION/SEIZU",
      "CRIMINAL MISCHIEF",
      "DEATH INVESTIGATION",
      "DETECTOR PROBLEM",
      "DHS REFERRAL",
      "DIABETIC PROBLEM",
      "DIABETIC",
      "DISTURBANCE",
      "DIZZY/VERTIGO",
      "DOMESTIC DISTURBANCE",
      "DUII",
      "DUMPSTER FIRE",
      "ELECTRICAL FIRE",
      "EXTRA PATROL COMPLETED",
      "EXTRA PATROL REQUEST",
      "F/A, ALL OTHER",
      "FAINT/CARDIAC HX",
      "FALL C1",
      "FALL",
      "FALL-ALPHA RESPONSE",
      "FALL-BRAVO RESPONSE",
      "FALL-DELTA RESPONSE",
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
      "GRASS FIRE",
      "HARASSMENT",
      "HAZARD",
      "HAZARDOUS MAT",
      "HEADACHE C1",
      "HEADACHE",
      "HEADACHE-CHARLIE RESPONSE",
      "HEART PROBLEM-CHARLIE RESPONSE",
      "HEART PROBLEM-DELTA RESPONSE",
      "HEART PROBLEMS",
      "HEAT EXPOSURE",
      "HEAT/COLD EXPOSU",
      "HIT AND RUN,NON INJURY",
      "IMMINENT BIRTH",
      "INCOMPLETE 911",
      "INFORMATION",
      "INVALID ASSIST",
      "JUVENILE ABUSE OR NEGLECT",
      "JUVENILE CUSTODY PROBLEM",
      "JUVENILE PROBLEM",
      "LANDING ZONE",
      "LIFT ASSIST",
      "LOCKOUT",
      "MARINE RESCUE 1",
      "MARINE RESCUE 2",
      "MEDICAL ALARM",
      "MEDICAL-DETAILS TO FOLLOW",
      "MENTAL",
      "MENTAL/PSYCH",
      "MISC FIRE",
      "MISC NO FIRE",
      "MISCELLANEOUS",
      "MISSING PERSON",
      "MOTORIST ASSIST",
      "MOVEUP",
      "MR1",
      "MR2",
      "MUTUAL AID",
      "MVA-INJURY ACCID",
      "MVA-UKN INJURY",
      "MVA-UNK INJURY",
      "MVA/UNKNOWN INJ",
      "NATURAL GAS LEAK",
      "NOISE COMPLAINT",
      "OBVIOUS DEATH",
      "ODOR INVESTIGATE",
      "ORDINANCE VIOLATION",
      "OVERDOSE/POISON",
      "PARKING COMPLAINT",
      "POLE FIRE",
      "PRE NOTIFICATION",
      "PREGNANCY/BIRTH",
      "PROPERTY INVESTIGATION",
      "PSYCHIATRIC",
      "PUBLIC ASSIST",
      "RECALL",
      "REQUEST FOR K-9 UNIT",
      "RESIDENTIAL FIRE ALARM",
      "RESIDENTIAL FIRE",
      "RFIRE",
      "ROBBERY,ARMED",
      "RUNAWAY JUVENILE",
      "SEIZE/CONTINUOUS",
      "SEIZURE-CHARLIE RESPONSE",
      "SEIZURE-DELTA RESPONSE",
      "SEIZURE-ECHO RESPONSE",
      "SEIZURES",
      "SHOOTING",
      "SICK PERSON C1",
      "SICK PERSON",
      "SICK PERSON/UNKO",
      "SMOKE INVESTIGAT",
      "SMOKE SMELL",
      "SPILL, FUEL/UNK",
      "STROKE",
      "STROKE-CHARLIE RESPONSE",
      "SUBJECT STOP",
      "SUICIDE ATTEMPT",
      "SUSPICIOUS CIRCUMSTANCES",
      "SUSPICIOUS PERSON",
      "SUSPICIOUS VEHICLE GONE",
      "SUSPICIOUS VEHICLE",
      "TAI",
      "TAI-HIGH MECHANI",
      "TAU",
      "TEST MEDICAL",
      "THEFT",
      "THEFT, JUST OCCURRED",
      "TRAFFIC ACCIDENT, INJURY",
      "TRAFFIC ACCIDENT,INJ",
      "TRAFFIC ACCIDENT,NON-INJURY",
      "TRAFFIC ACCIDENT,UNK INJURY",
      "TRAFFIC COMPLAINT",
      "TRAFFIC COMPLAINT",
      "TRAFFIC STOP",
      "TRAFFIC STOP",
      "TRANSFER/INTERFA",
      "TRAUMA C1",
      "TRAUMA",
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
      "UNCONS/UNRESPONS",
      "UNCONSC/FAINTING",
      "UNCONSCIO/AGONAL",
      "UNCONSCIOUS/FAINTING-CHARLIE",
      "UNCONSCIOUS/FAINTING-DELTA",
      "UNK PROB/MN DOWN",
      "UNKNOWN PROBLEM-BRAVO RESP",
      "UNKNOWN TYP FIRE",
      "UNWANTED",
      "VEHICLE FIRE",
      "VEHICLE RELEASE",
      "VIOLATION OF RESTRAINING ORDER",
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
