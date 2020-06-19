package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class MOLincolnCountyAParser extends DispatchA25Parser {
  
  public MOLincolnCountyAParser() {
    this("LINCOLN COUNTY", "MO");
  }
  
  public MOLincolnCountyAParser(String defCity, String defState) {
    super(defCity, defState);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getAliasCode() {
    return "MOLincolnCounty";
  }
  
  @Override
  public String getFilter() {
    return "lincolncounty911@LC911Dispatch.org,lincolncounty911@lcsomo.com,messaging@iamresponding.com,EnterpolAlerts@PikeCountySO.or";
  }
  
  private static final Pattern ELSBERRY_PTN = Pattern.compile("(NEW .*)(?: - |, )(Elsberry).?");
  private static final Pattern DIRO_PTN = Pattern.compile("\\b([NSEW])[/ ](O)F?\\b", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    subject = stripFieldStart(subject, "[txtinfo]");
    
    // Elsberry Fire has a slight variant on one of the unusual alternate formats
    Matcher match = ELSBERRY_PTN.matcher(body);
    if (match.matches()) body = match.group(1) + ", " + match.group(2);
    
    // Fix dir/o construct
    body = DIRO_PTN.matcher(body).replaceAll("$1$2");
    
    // We have to eliminate the call description taht includes a dash delimiter
    body =  body.replace("", "");
    
    // TODO Auto-generated method stub
    return super.parseMsg(subject, body, data);
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "APPLE GROVE",
      "AUTUMN OAKS",
      "BEAU MAYE",
      "BETHEL CREEK",
      "BLUE BUNTING",
      "BURR OAK",
      "BUSINESS PARK",
      "CAMP CUIVRE",
      "CAP AU GRIS",
      "CHAPMAN FARM",
      "COLLEGE CAMPUS",
      "COLTON JESSE",
      "CORNER STONE CHURCH",
      "COUNTY FARM",
      "CUIVRE FORD",
      "DEER RUN",
      "DEER VALLEY",
      "DON WILLIAMS",
      "DUNCAN MANSION",
      "ELM TREE",
      "FAIRVIEW CHURCH",
      "FOX RUN",
      "FRED NORTON",
      "FRIENDSHIP VILLAGE",
      "GLEN FOREST",
      "GOODWOOD FARMS",
      "HAWKS RIDGE",
      "HICKORY FOREST",
      "HICKORY HILL",
      "HIDDEN OAKS",
      "HIDDEN VALLEY",
      "HIGH COUNTRY",
      "JERRY MILLER",
      "JOHN I WILSON",
      "LAKE FOREST",
      "LIDDIE LOU",
      "LINCOLN HILLS",
      "MAPLE SHADE",
      "MARKET PLACE",
      "MARY TOM",
      "MCINTOSH HILL",
      "MEADOW RIDGE",
      "MOORE SCHOOL",
      "MOSCOW MILLS",
      "NO WHITE",
      "OAK BEND",
      "OAK FOREST",
      "OWLS REST",
      "PAGE BRANCH",
      "PALACE WAY",
      "PATTERSON SCHOOL",
      "PEAR ORCHARD",
      "PEBBLE CREEK",
      "PEINE LAKES",
      "PIN OAK",
      "PORT AU PRINCE",
      "PUMP HOUSE",
      "QUAIL MEADOWS",
      "QUAIL POINT",
      "ROCK SPRINGS",
      "ROYAL OAK",
      "RUNNING RIVER",
      "SADDLEBACK RIDGE",
      "SALLY JANE",
      "SANDY SLOUGH",
      "SANTA CRUZ",
      "SITTON BRANCH",
      "SNOW HILLS",
      "SPRING HOUSE",
      "ST ALPHONSUS",
      "ST MICHELE",
      "ST STEPHEN",
      "STAR HOPE",
      "SUGAR RIDGE",
      "SUN SWEPT",
      "TAYLOR SCHOOL",
      "THORNHILL CEMETERY",
      "TRUXTON CHURCH",
      "VALLEY GREEN",
      "VILLAGE CIRCLE",
      "WEST LOOP",
      "WHITE WILDLIFE",
      "WINDY HILL",
      "WOLF PEN HOLLOW"
  };

  
  private static final CodeSet CALL_LIST = new CodeSet(
      "911 HANG UP/ACCIDENTAL",
      "AMBULANCE TRANSFER",
      "ABDOMINAL PAIN/PROBLEMS",
      "ADMIN/REPO/EXTRA PATROL/CIVIL",
      "AIRCRAFT EMERGENCY",
      "ALARM",
      "ALARM - FIRE & DETECTORS",
      "ALARM - RESIDENTIAL",
      "ALARMS",
      "ALARMS (FIRE)",
      "ALLERGIES",
      "ANIMAL",
      "ASSIST MOTORIST NB",
      "ASSIST OTHER AGENCIES LINCOLN CO FIRE DIST #1",
      "ASSIST OTHER AGENCIES",
      "ASSIST - CITIZEN",
      "ASSIST - OTHER AGENCY",
      "BACK PAIN",
      "BREATHING PROBLEMS",
      "C&I DRIVER",
      "CARDIAC OF RESPIRATORY ARREST",
      "CHECK WELL BEING",
      "CHEST PAIN (NON-TRAUMATIC)",
      "CHOKING",
      "CITIZEN ASSIST/SERVICE CALL",
      "CONTROL BURN",
      "CONTROL BURN NOTICE",
      "CONTROLLED BURN NOTICE",
      "CONVULSIONS/SEIZURES",
      "DIABETIC PROBLEMS",
      "DISABLED VEHICLE",
      "DOMESTIC DIST IN PROGRESS",
      "DOMESTIC DISTURBANCE/VIOLENCE",
      "ELECTRICAL HAZARD",
      "EXPLOSION",
      "EXTRICATION/ENTRAPPED",
      "FALLS",
      "FIRE",
      "FIRE - MARINE SEARCH & RESCUE",
      "FIRE ALARM COMMERCIAL",
      "FIRE NON RESIDENTIAL/STRUCTURE",
      "FIRE-CARBON MONOXIDE ALARM",
      "FIRE-NATURAL COVER",
      "FIRE-RESIDENTIAL",
      "FIRE-VEHICLE",
      "FOLLOW UP",
      "FOLLOW UP - INVESTIGATION",
      "FUEL SPILL",
      "GAS LEAK/GAS ODOR",
      "HEART PROBLEMS/A.I.C.D.",
      "HEMORRHAGE/LACERATION",
      "INFORMATION",
      "INTERFACILITY EVAL/TRANSFER",
      "JUVENILE CALL",
      "HAZARD - CONDITIONS",
      "HAZMAT",
      "LIGHTENING STRIKE (INVESTIGATION)",
      "MEDICAL - AID",
      "MISSING/RUNAWAY/FOUND PERSON",
      "MISSING PERSON",
      "MOTOR VEHICLE ACCIDENT REPORT",
      "MOTOR VEHICLE COLLISION",
      "MOTOR VEHICLE COLLISION (LAW)",
      "MOTOR VEHICLE COLLISION (MED)",
      "MOTOR VEHICLE SLIDE OFF",
      "MOTOR VEHICLE VS. DEER",
      "MUTUAL AID/ASSIST OUTSIDE AGENCY",
      "MVC - FAIL TO REMAIN",
      "MVC - INSURANCE REPORT",
      "MVC - PERSONAL INJURY",
      "MVC - PROPERTY DAMAGE",
      "ODOR (STRANGE/UNKNOWN)",
      "OUTSIDE AGENCY ASSIST",
      "OUTSIDE FIRE",
      "OVER DOSE",
      "OVERDOSE/POISONING (INGESTION)",
      "PSR/911 HANG UP/CWB",
      "PSYCHIATRIC/ABNORMAL BEHAVIOR",
      "ROAD CLOSED",
      "SICK PERSON",
      "SMOKE INVESTIGATION (OUTSIDE)",
      "SPECIAL ASSIGNMENT",
      "SPECIAL EVENTS",
      "STRCUTURE FIRE (APPLIANCE)",
      "STRUCTURE FIRE BLDG-STRUCTURE",
      "STRUCTURE FIRE ODOR OF SMOKE",
      "STRUCTURE FIRE VISIBLE SMOKE",
      "STROKE",
      "STRUCTURE FIRE",
      "SUICIDAL PERSON/ATT. SUICIDE",
      "SUICIDE",
      "SUSPICIOUS ACTIVITY",
      "TEST (FIRE) TEST CALL*******",
      "TEST (FIRE) TEST TONES******",
      "TEST LINCOLN CO 911",
      "THEFT - EARLIER",
      "THREAT SUICIDE",
      "TRAFFIC/TRANSPORT ACCIDENTS",
      "TRAFFIC HAZARD",
      "TRAFFIC VIOL/HAZARD/ABANDONED",
      "TRAINING",
      "TRAUMATIC INJURIES (SPECIFIC)",
      "TRESPASSING",
      "UNCONSCIOUS/FAINTING (NEAR)",
      "UNKNOWN (3RD PARTY)",
      "UNKNOWN PROBLEMS (MAN DOWN)",
      "UNKNOWN TROUBLE",
      "UTILITY OUTAGE OR PROBLEM",
      "VEHICLE FIRE",
      "VEHICLE LOCKOUT",
      "WATER RESCUE",
      "WEATHER"
 );
}
