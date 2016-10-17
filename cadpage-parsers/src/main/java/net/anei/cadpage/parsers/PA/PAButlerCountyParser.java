package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class PAButlerCountyParser extends DispatchB2Parser {
  
  private static final Pattern MARKER = Pattern.compile(" Cad: \\d{4}-\\d{10}$");
  private static final Pattern NAME_COUNTY_PTN = Pattern.compile("([A-Z]+)\\b(?!,) *(?:BEAVER *)?(?:CO(?:UNTY)?)? *(?:911|9-1-1)? *(.*)");

  public PAButlerCountyParser() {
    super("BUTLER911:", CITY_LIST, "BUTLER COUNTY", "PA", B2_CROSS_FOLLOWS);
    setupSaintNames("BARNABAS","JOHNS");
    removeWords("TURNPIKE");
    setupMultiWordStreets(
        "ADAMS POINTE",
        "ADAMS RIDGE",
        "ADAMS SHOPPES",
        "ALAMEDA PARK",
        "BEACON LIGHT",
        "BEAHM CREST",
        "BEAR CREEK",
        "BENT CREEK",
        "BLACKBERRY HILL",
        "BLUE SPRUCE",
        "BON AIRE",
        "BOY SCOUT",
        "BRIER HILL",
        "BROWNS HILL",
        "BROWNS MILL",
        "BUTLER COMMONS",
        "CAMP TREES",
        "CANNEL COAL",
        "CARBON CENTER",
        "CHAPEL RIDGE",
        "CHERRY VALLEY",
        "CHICORA FENELTON",
        "CHRIS BEL",
        "COMMERCE PARK",
        "COMMONS OF SAXONBURG",
        "COUNTRY SIDE",
        "CRANBERRY SQUARE",
        "CRITCHLOW SCHOOL",
        "DEAHL CREEK",
        "DEER CREEK",
        "EAST BUTLER",
        "EAST RIDGE",
        "EAST VANDERBILT",
        "EVANS CITY",
        "EVERGREEN MILL",
        "FAIRGROUND HILL",
        "FREW MILL",
        "GLADE MILL",
        "GREAT BELT",
        "HAINE SCHOOL",
        "JO DEENER",
        "LAKE ARTHUR",
        "LEGION MEMORIAL",
        "LITTLE CREEK",
        "LOST VALLEY",
        "MARS EVANS CITY",
        "MARS VALENCIA",
        "MARY LYNN",
        "MCCALIP FARM",
        "MEDICAL CENTER",
        "MORAINE POINTE",
        "MORNING GROVE",
        "MOUNTAIN VIEW",
        "MUDDY CREEK",
        "NEEDLE POINT",
        "NEW CASTLE",
        "NEW HAVEN",
        "NORTH BOUNDARY",
        "NORTH WASHINGTON",
        "NORWEGIAN SPRUCE",
        "OAK PARK",
        "OAK RIDGE",
        "ONEIDA VALLEY",
        "PA TPKE",
        "PEACEFUL VALLEY",
        "PINE HAVEN",
        "PINE RIDGE",
        "QUEEN JUNCTION",
        "RADER SCHOOL",
        "ROLLING MILLS",
        "SAINT FRANCIS",
        "SAINT IVES",
        "SANDY HILL",
        "SANDY POINT",
        "SCOTT RIDGE",
        "SENCA SCHOOL",
        "SETTLERS VILLAGE",
        "SEVEN FIELDS",
        "SEVEN HILLS",
        "SLIPPERY ROCK",
        "SOUTHERN VALLEY",
        "SPITHALER SCHOOL",
        "STAR VIEW",
        "STEINER BRIDGE",
        "STONEY HOLLOW",
        "SUGAR VALLEY",
        "SWAIN HILL",
        "THORN HILL",
        "THREE DEGREE",
        "TOWNSHIP LINE",
        "VAN BUREN",
        "VISTA RIDGE",
        "WATTERS STATION",
        "WEST JEFFERSON",
        "WEST PARK",
        "WHITE PINE",
        "WILLIAM FLYNN");
  }
  
  @Override
  public String getFilter() {
    return "@butlerco.911,@co.butler.pa.us";
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = PA_TPKE_PTN.matcher(addr).replaceAll("PENNSYLVANIA TURNPIKE");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern PA_TPKE_PTN = Pattern.compile("\\bPA (?:TPKE|TURNPIKE)\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected boolean isPageMsg(String body) {
    return MARKER.matcher(body).find();
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    
    if (body.startsWith(":")) body = "BUTLER911" + body;
    if (! super.parseMsg(body, data)) return false;
    
    data.strCity = stripFieldEnd(data.strCity, " BORO");
    if (data.strCity.endsWith(" T")) data.strCity += "WP";
    data.strCity =  convertCodes(data.strCity, CITY_CONVERSION_TABLE);
    
    if (data.strName.equals("NOT ENTERED")) data.strName = "";
    else {
      data.strName = stripFieldStart(data.strName, "-");
      Matcher match = NAME_COUNTY_PTN.matcher(data.strName);
      if (match.matches()) {
        String county = COUNTY_CONVERSION_TABLE.getProperty(match.group(1));
        if (county != null) {
          data.strCity = append(data.strCity, ", ", county);
          data.strName = match.group(2);
        }
      }
    }
    if (data.strMap.equals("00000,000")) data.strMap = "";
    return true;
  }
  
  protected CodeSet buildCallList() {
    return new CodeSet(
        "ABDOMINAL PAIN",
        "ABDOMINAL PAIN",
        "ABNORMAL BREATHING",
        "ABNORMAL BREATHING (PARTIAL OB",
        "ACCIDENT",
        "AIRCRAFT CRASH / DISTRESS",
        "ALARM/FIRE",
        "ALARM / SPRINKLER",
        "ALERT AND BEHAVING NORMALLY",
        "ALERT AND BREATHING NORMALLY",
        "ALERT WITH ABNORMAL BREATHING",
        "ALLERGIC REACTION",
        "ALLERGIES / ENVENOMATIONS",
        "ALTERED LEVEL OF CONSCIOUSNESS",
        "ASSAULT / NO INJURIES",
        "ASSIST POLICE",
        "ASSIST WITH EMS",
        "ASSIST WITH FIRE",
        "ATYPICAL SEIZURE",
        "AUTO ALARM TEST",
        "BACK PAIN",
        "BLEEDING",
        "BLOOD PRESSURE ABNORMALITY (AS",
        "BLOOD THINNERS",
        "BOATING EMERGENCY",
        "BREATHING NORMALLY",
        "BREATHING NORMALLY < 35",
        "BREATHING NORMALLY => 35",
        "BREATHING PROBLEMS",
        "BREATHING PROBLEMS",
        "BREATHING UNCERTAIN (AGONAL)",
        "BURNING COMPLAINT",
        "CARDIAC HISTORY",
        "CARDIAC OR RESPIRATORY ARREST",
        "CATHETER (IN/OUT WITHOUT HEMOR",
        "CHECK WELL BEING",
        "CHEST OR NECK INJURY (WITH DIF",
        "CHEST PAIN",
        "CHEST PAIN",
        "CLAMMY",
        "CO DETECTOR / FIRE RESPONSE",
        "CO DETECTOR / PEOPLE ILL",
        "CONSTIPATION",
        "CONVULSIONS / SEIZURES",
        "DANGEROUS HEMORRHAGE",
        "DIABETIC PROBLEMS",
        "DIFFICULTY BREATHING OR SWALLO",
        "DIFFICULTY SPEAKING BETWEEN BR",
        "DISABLED VEHICLE",
        "DISTURBANCE",
        "DIZZINESS/VERTIGO",
        "DRILL",
        "EMERGENCY RESPONSE REQUESTED",
        "EMS STAND BY",
        "EMS TRANSPORT",
        "EXPLOSION",
        "FAINTING EPISODE(S) AND ALERT",
        "FALLS",
        "FALLS",
        "FEMALES WITH PAIN ABOVE NAVEL",
        "FEVER/CHILLS",
        "FIRE / GRASS OR BRUSH",
        "FIRE / CHIMNEY",
        "FIRE / POLE",
        "FIRE / UNKNOWN",
        "FIRE - STRUCTURE",
        "FIRE - VEHICLE",
        "FLOODING / BASEMENT",
        "FLOODING / ROAD",
        "GENERAL WEAKNESS",
        "HAZARDOUS ROAD CONDITION",
        "HEAD VISIBLE/OUT",
        "HEART ATTACK OR ANGINA HISTORY",
        "HEART PROBLEMS",
        "HEAT / COLD EXPOSURE",
        "HISTORY OF SEVERE ALLERGIC REA",
        "INEFFECTIVE BREATHING",
        "INJURED PERSON",
        "MALES WITH PAIN ABOVE NAVEL => 223",
        "MALES WITH PAIN ABOVE NAVEL => 376",
        "MEDICAL ALARM (ALERT) NOTIFICA",
        "MEDICAL ALARM",
        "MENTAL PATIENT",
        "MODERATE EYE INJURIES",
        "NAUSEA",
        "NO DIFFICULTY BREATHING OR SWA",
        "NON-RECENT (=> 6HRS) INJURIES",
        "NON-SUICIDAL AND ALERT",
        "NON-TRAUMATIC BACK PAIN",
        "NO PRIORITY SYMPTOMS (COMPLAIN",
        "NO PRIORITY SYMPTOMS (COMPLAIN `85",
        "NOT ALERT",
        "NOT BREATHING AT ALL",
        "NOT DANGEROUS BODY AREA",
        "NOT DANGEROUS HEMORRHAGE",
        "NOTIFICATION - EMS",
        "NOTIFICATION - FIRE",
        "NOT SEIZING NOW AND EFFECTIVE",
        "ODOR OF GAS IN A RESIDENCE",
        "OTHER PAIN",
        "OVERDOSE / POISONING (INGESTIO",
        "PERSON MISSING",
        "POSSIBLY DANGEROUS BODY AREA",
        "POSSIBLY DANGEROUS HEMORRHAGE",
        "PROBLEM UNKNOWN",
        "PSYCHIATRIC",
        "PSYCHIATRIC / ABNORMAL BEHAVIO",
        "PSYCHIATRIC/OVERDOSE",
        "PSYCHIATRIC PROBLEM",
        "PUBLIC ASSIST (NO INJURIES AND",
        "RECKLESS DRIVING",
        "ROAD HAZARD",
        "SEIZURES/CONVULSIONS",
        "SERIOUS HEMORRHAGE",
        "SERVICE CALL",
        "SICK PERSON",
        "SMOKE INVESTIGATION",
        "STANDING SITTING MOVING OR TAL",
        "STROKE (CVA)",
        "STROKE/CVAV/TIA",
        "SUDDEN ONSET OF SEVERE PAIN",
        "SUDDEN SPEECH PROBLEMS",
        "SUSPICIOUS ACTIVITY",
        "SWARMING ATTACK (BEE WASP HORN",
        "TOOTHACHE (WITHOUT JAW PAIN)",
        "TRAFFIC CONTROL",
        "TRAFFIC / TRANSPORTATION ACCID",
        "TRANSPORTATION ONLY",
        "UNCONSCIOUS",
        "UNCONSCIOUS -- EFFECTIVE BREAT",
        "UNCONSCIOUS OR ARREST",
        "UNKNOWN STATUS/OTHER CODES NOT",
        "VA/ENTRAPMENT",
        "VA / FIRE",
        "VA / INJURIES",
        "VA / MOTORCYCLE",
        "VA / MOTORCYCLE EAST",
        "VA / UNKNOWN INJURY",
        "VA / UNKNOWN INJURY WILLIAM",
        "VEHICLE ACCIDENT",
        "WIRES DOWN",
        "WOUND INFECTED (FOCAL OR SURFA"
    );
  }
  
  private static final String[] CITY_LIST = new String[]{
    "BUTLER",
    
    "BRUIN BORO",
    "CALLERY BORO",
    "CHERRY VALLEY BORO",
    "CHICORA BORO",
    "CONNOQ BORO",
    "CONNOQUENESSING BORO",
    "EAST BUTLER BORO",
    "EAU CLAIRE BORO",
    "EVANS CITY BORO",
    "FAIRVIEW BORO",
    "HARMONY BORO",
    "HARRISVILLE BORO",
    "KARNS CITY BORO",
    "MARS BORO",
    "PETROLIA BORO",
    "PORTERSVILLE BORO",
    "PROSPECT BORO",
    "SAXONBURG BORO",
    "SEVEN FIELDS BORO",
    "SLIPPERY ROCK BORO",
    "VALENCIA BORO",
    "WEST LIBERTY BORO",
    "WEST SUNBURY BORO",
    "ZELIENOPLE BORO",
    
    "ADAMS TWP",
    "ALLEGHENY TWP",
    "BRADY TWP",
    "BUFFALO TWP",
    "BUTLER TWP",
    "CENTER TWP",
    "CHERRY TWP",
    "CLAY TWP",
    "CLEARFIELD TWP",
    "CLINTON TWP",
    "CONCORD TWP",
    "CONNOQ TWP",
    "CONNOQUENESSING TWP",
    "CRANBERRY TWP",
    "DONEGAL TWP",
    "FAIRVIEW TWP",
    "FORWARD TWP",
    "FRANKLIN TWP",
    "JACKSON TWP",
    "JEFFERSON TWP",
    "LANCASTER TWP",
    "MARION TWP",
    "MERCER TWP",
    "MIDDLESEX TWP",
    "MUDDY CREEK TWP",
    "MUDDYCREEK TWP",   // Dispatch typo
    "OAKLAND TWP",
    "PARKER TWP",
    "PENN TWP",
    "SLIPPERY ROCK T",
    "SLIPPERY ROCK TWP",
    "SUMMIT TWP",
    "VENANGO TWP",
    "WASHINGTON TWP",
    "WINFIELD TWP",
    "WORTH TWP",
    
    "BOYERS",
    "EIDENAU",
    "HILLIARDS",
    "MURRINSVILLE",
    "RENFREW",
    "SARVER",
    "WAHLVILLE",
    "WATTERS",
    
    // Counties
    "ALLEGHENY COUNTY",
    "BEAVER COUNTY",
    "CLARION COUNTY",
    "LAWRENCE COUNTY",
    "MERCER COUNTY",
    "VENANGO COUNTY",
    "WESTMORELAND COUNTY",
    
    // out of county locations
    "ELLWOOD CITY",
    "FOXBERG BORO",
    "HOVE TWP",
    "MARSHALL T", 
    "MARSHALL TWP",
    "MARSHALL",
    "NEW SEWICKLEY",
    "PARKER CITY",
    "PERRY TWP",
    "PINE TWP",
    "RICHLAND",
    "RICHLAND TWP",
    "SUGARCREEK REST",
    "SUGARCREEK T",
    "SUGARCREEK TWP"
  };
  
  private static final Properties CITY_CONVERSION_TABLE = buildCodeTable(new String[]{
      "CONNOQ",                   "CONNOQUENESSING",
      "CONNOQ TWP",               "CONNOQUENESSING TWP",
      "MARSHALL T",               "MARSHALL TWP", 
      "MARSHALL",                 "MARSHALL TWP",
      "MUDDYCREEK TWP",           "MUDDY CREEK TWP",
      "RICHLAND",                 "RICHLAND TWP",
      "SUGARCREEK REST",          "SUGARCREEK TWP",
      "SUGARCREEK T",             "SUGARCREEK TWP"
  });
  
  private static final Properties COUNTY_CONVERSION_TABLE = buildCodeTable(new String[]{
      "AGC",            "ALLEGHENY COUNTY",
      "ALLEG",          "ALLEGHENY COUNTY",
      "ALLEGHENY",      "ALLEGHENY COUNTY",
      "ARMSTRONG",      "ARMSTRONG COUNTY",
      "BEAVER",         "BEAVER COUNTY",
      "CLARION",        "CLARION COUNTY",
      "LAWRENCE",       "LAWRENCE COUNTY",
      "LEOC",           "LAWRENCE COUNTY",
      "LEOZ",           "LAWRENCE COUNTY",
      "MERCER",         "MERCER COUNTY",
      "VENANGO",        "VENANGO COUNTY",
      "WESTMORELAND",   "WESTMORELAND COUNTY"
      
  });
}
