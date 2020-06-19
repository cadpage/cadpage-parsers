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
      "TRAILER INSPECTION",
      "TRUXTON CHURCH",
      "VALLEY GREEN",
      "VILLAGE CIRCLE",
      "WEST LOOP",
      "WHITE WILDLIFE",
      "WINDY HILL",
      "WOLF PEN HOLLOW"
  };

  
  private static final CodeSet CALL_LIST = new CodeSet(
      "911 ABANDONED CALL",
      "911 ACCIDENTAL",
      "911 HANG UP/ACCIDENTAL",
      "911 OPEN LINE",
      "911 TRANSFER",
      "ABDOMINAL PAIN/PROBLEMS",
      "ADMIN/REPO/EXTRA PATROL/CIVIL",
      "AIRCRAFT EMERGENCY",
      "ALARM - FIRE & DETECTORS",
      "ALARM - RESIDENTIAL",
      "ALARM",
      "ALARM-COMERCIAL",
      "ALARM-COMMERCIAL",
      "ALARM-RESIDENTIAL",
      "ALARMS (FIRE)",
      "ALARMS",
      "ALLERGIES",
      "AMBULANCE TRANSFER",
      "ANIMAL COMPLAINT",
      "ANIMAL",
      "ARSON-RESIDENCE",
      "ASSIST - CITIZEN",
      "ASSIST - OTHER AGENCY",
      "ASSIST MOTORIST NB",
      "ASSIST OTHER AGENCIES",
      "ASSIST OTHER AGENCY",
      "BACK PAIN",
      "BREATHING PROBLEMS",
      "C&I DRIVER",
      "CARDIAC OF RESPIRATORY ARREST",
      "CHAMP CLARK",
      "CHECK WELL BEING",
      "CHEST PAIN (NON-TRAUMATIC)",
      "CHOKING",
      "CITIZEN ASSIST/SERVICE CALL",
      "CIVIL ISSUE",
      "CONTROL BURN NOTICE",
      "CONTROL BURN",
      "CONTROLLED BURN NOTICE",
      "CONVULSIONS/SEIZURES",
      "COURT",
      "DIABETIC PROBLEMS",
      "DISABLED VEHICLE",
      "DOMESTIC DIST IN PROGRESS",
      "DOMESTIC DISTURBANCE/VIOLENCE",
      "DRIVE COMPLAINT - C&I",
      "ELECTRICAL HAZARD",
      "EXPLOSION",
      "EXTRA PATROL",
      "EXTRICATION/ENTRAPPED",
      "FALLS",
      "FIRE - MARINE SEARCH & RESCUE",
      "FIRE ALARM COMMERCIAL",
      "FIRE ALARM RESIDENTIAL",
      "FIRE NON RESIDENTIAL/STRUCTURE",
      "FIRE",
      "FIRE-CARBON MONOXIDE ALARM",
      "FIRE-COMMERCIAL",
      "FIRE-GAS LEAK",
      "FIRE-NATURAL COVER",
      "FIRE-RESIDENTIAL",
      "FIRE-VEHICLE",
      "FIRE-WIRES DOWN",
      "FOLLOW UP - INVESTIGATION",
      "FOLLOW UP",
      "FUEL SPILL",
      "GAS LEAK/GAS ODOR",
      "HANDLE BY PHONE",
      "HARASS - PHONE & EMAIL",
      "HAZARD - CONDITIONS",
      "HAZMAT",
      "HEART PROBLEMS/A.I.C.D.",
      "HEMORRHAGE/LACERATION",
      "INFORMATION",
      "INTERFACILITY EVAL/TRANSFER",
      "JUVENILE CALL",
      "K-9 SEARCH",
      "LIGHTENING STRIKE (INVESTIGATION)",
      "MEDAID - LIFT ASSIST",
      "MEDAID - MUTUAL AID",
      "MEDICAL - AID",
      "MEDICAL - TRANSFER",
      "MEDICAL MA - MUTUAL AID",
      "MISSING PERSON",
      "MISSING/RUNAWAY/FOUND PERSON",
      "MOTOR VEHICLE ACCIDENT REPORT",
      "MOTOR VEHICLE COLLISION (LAW)",
      "MOTOR VEHICLE COLLISION (MED)",
      "MOTOR VEHICLE COLLISION",
      "MOTOR VEHICLE SLIDE OFF",
      "MOTOR VEHICLE VS. DEER",
      "MUTUAL AID/ASSIST OUTSIDE AGENCY",
      "MVC - FAIL TO REMAIN",
      "MVC - INSURANCE REPORT",
      "MVC - PERSONAL INJURY",
      "MVC - PROPERTY DAMAGE",
      "ODOR (STRANGE/UNKNOWN)",
      "OFF DUTY",
      "ON DUTY",
      "OUTSIDE AGENCY ASSIST",
      "OUTSIDE FIRE",
      "OVER DOSE",
      "OVERDOSE/POISONING (INGESTION)",
      "PARKING COMPLAINT",
      "PERSON - J2",
      "PLATES - J1",
      "PSR/911 HANG UP/CWB",
      "PSYCHIATRIC/ABNORMAL BEHAVIOR",
      "ROAD CLOSED",
      "ROLLLING HILLS",
      "SICK PERSON",
      "SMOKE INVESTIGATION (OUTSIDE)",
      "SPECIAL ASSIGNMENT",
      "SPECIAL EVENTS",
      "ST CHARLES",
      "STRCUTURE FIRE (APPLIANCE)",
      "STROKE",
      "STRUCTURE FIRE BLDG-STRUCTURE",
      "STRUCTURE FIRE ODOR OF SMOKE",
      "STRUCTURE FIRE VISIBLE SMOKE",
      "STRUCTURE FIRE",
      "SUCIDIAL THREAT",
      "SUICIDAL PERSON/ATT. SUICIDE",
      "SUICIDE",
      "SUSPICIOUS - PERSON",
      "SUSPICIOUS - VEHICLE",
      "SUSPICIOUS ACTIVITY",
      "TELLA JANE",
      "TEST (FIRE) TEST CALL*******",
      "TEST (FIRE) TEST TONES******",
      "TEST LINCOLN CO 911",
      "TEST",
      "THEFT - EARLIER",
      "THREAT SUICIDE",
      "TOWN CENTER",
      "TRAFFIC - STOP",
      "TRAFFIC HAZARD",
      "TRAFFIC VIOL/HAZARD/ABANDONED",
      "TRAFFIC/TRANSPORT ACCIDENTS",
      "TRAILER INSPECTION",
      "TRAINING",
      "TRAUMATIC INJURIES (SPECIFIC)",
      "TREE DOWN",
      "TRESPASSING",
      "UNCONSCIOUS/FAINTING (NEAR)",
      "UNKNOWN (3RD PARTY)",
      "UNKNOWN PROBLEMS (MAN DOWN)",
      "UNKNOWN TROUBLE",
      "UTILITY OUTAGE OR PROBLEM",
      "VEHICLE FIRE",
      "VEHICLE LOCKOUT",
      "WATER RESCUE",
      "WATER STANDBY LOUISIANA",
      "WEATHER",
      "WELL BEING CHECK",
      "ZONE PATROL"
  );
  
}
