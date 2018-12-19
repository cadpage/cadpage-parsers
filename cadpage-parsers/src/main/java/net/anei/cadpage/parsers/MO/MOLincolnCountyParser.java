package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class MOLincolnCountyParser extends DispatchA25Parser {
  
  public MOLincolnCountyParser() {
    this("LINCOLN COUNTY", "MO");
  }
  
  public MOLincolnCountyParser(String defCity, String defState) {
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
      "BEAU MAYE",
      "BLUE BUNTING",
      "BURR OAK",
      "CAP AU GRIS",
      "CHAPMAN FARM",
      "COLLEGE CAMPUS",
      "COLTON JESSE",
      "CORNER STONE CHURCH",
      "DEER RUN",
      "DEER VALLEY",
      "DUNCAN MANSION",
      "ELM TREE",
      "FOX RUN",
      "FRIENDSHIP VILLAGE",
      "HAWKS RIDGE",
      "HICKORY HILL",
      "HIDDEN VALLEY",
      "HIGH COUNTRY",
      "LIDDIE LOU",
      "MARKET PLACE",
      "MARY TOM",
      "MEADOW RIDGE",
      "MOORE SCHOOL",
      "MOSCOW MILLS",
      "NO WHITE",
      "OAK BEND",
      "PEBBLE CREEK",
      "PEINE LAKES",
      "PIN OAK",
      "PUMP HOUSE",
      "SADDLEBACK RIDGE",
      "SITTON BRANCH",
      "SNOW HILLS",
      "SPRING HOUSE",
      "ST STEPHEN",
      "TAYLOR SCHOOL",
      "THORNHILL CEMETERY",
      "WEST LOOP",
      "WHITE WILDLIFE",
      "WINDY HILL"

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
      "ASSIST MOTORIST NB",
      "ASSIST - CITIZEN",
      "ASSIST - OTHER AGENCY",
      "BREATHING PROBLEMS",
      "C&I DRIVER",
      "CARDIAC OF RESPIRATORY ARREST",
      "CHECK WELL BEING",
      "CHEST PAIN (NON-TRAUMATIC)",
      "CHOKING",
      "CITIZEN ASSIST/SERVICE CALL",
      "CONTROL BURN",
      "CONTROL BURN NOTICE",
      "CONVULSIONS/SEIZURES",
      "DIABETIC PROBLEMS",
      "DISABLED VEHICLE",
      "DOMESTIC DISTURBANCE/VIOLENCE",
      "ELECTRICAL HAZARD",
      "EXTRICATION/ENTRAPPED",
      "FALLS",
      "FIRE",
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
      "MVC - INSURANCE REPORT",
      "MVC - PERSONAL INJURY",
      "MVC - PROPERTY DAMAGE",
      "ODOR (STRANGE/UNKNOWN)",
      "OUTSIDE AGENCY ASSIST",
      "OUTSIDE FIRE",
      "OVER DOSE",
      "OVERDOSE/POISONING (INGESTION)",
      "PSYCHIATRIC/ABNORMAL BEHAVIOR",
      "ROAD CLOSED",
      "SICK PERSON",
      "SMOKE INVESTIGATION (OUTSIDE)",
      "SPECIAL ASSIGNMENT",
      "SPECIAL EVENTS",
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
      "VEHICLE LOCKOUT"
 );
}
