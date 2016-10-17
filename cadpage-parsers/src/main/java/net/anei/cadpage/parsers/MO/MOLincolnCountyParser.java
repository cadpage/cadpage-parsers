package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class MOLincolnCountyParser extends DispatchA25Parser {
  
  private static final Pattern ELSBERRY_PTN = Pattern.compile("(NEW .*)(?: - |, )(Elsberry).?");
  public MOLincolnCountyParser() {
    super("LINCOLN COUNTY", "MO");
    setupCallList(CALL_LIST);
  }
  
  @Override
  public String getFilter() {
    return "EnterpolAlerts@PikeCountySO.org,lincolncounty911@LC911Dispatch.org,messaging@iamresponding.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Elsberry Fire has a slight variant on one of the unusual alternate formats
    Matcher match = ELSBERRY_PTN.matcher(body);
    if (match.matches()) body = match.group(1) + ", " + match.group(2);
    
    // TODO Auto-generated method stub
    return super.parseMsg(subject, body, data);
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "911 HANG UP/ACCIDENTAL",
      "ALARM",
      "BREATHING PROBLEMS",
      "CHECK WELL BEING",
      "CHEST PAIN (NON-TRAUMATIC)",
      "C&I DRIVER",
      "CITIZEN ASSIST/SERVICE CALL",
      "CONVULSIONS/SEIZURES",
      "DOMESTIC DISTURBANCE/VIOLENCE",
      "ELECTRICAL HAZARD",
      "FALLS",
      "HEART PROBLEMS/A.I.C.D.",
      "HEMORRHAGE/LACERATION",
      "MOTOR VEHICLE ACCIDENT REPORT",
      "ODOR (STRANGE/UNKNOWN)",
      "OUTSIDE FIRE",
      "OVERDOSE/POISONING (INGESTION)",
      "SICK PERSON",
      "SMOKE INVESTIGATION (OUTSIDE)",
      "SPECIAL ASSIGNMENT",
      "STROKE",
      "STRUCTURE FIRE",
      "SUSPICIOUS ACTIVITY",
      "TRAFFIC HAZARD",
      "TRAFFIC/TRANSPORT ACCIDENTS",
      "UNCONSCIOUS/FAINTING (NEAR)",
      "VEHICLE FIRE",
      "VEHICLE LOCKOUT"
 );
}
