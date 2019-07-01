package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;


public class VACampbellCountyAParser extends DispatchDAPROParser {
  
  public VACampbellCountyAParser() {
    super(CITY_CODES, "CAMPBELL COUNTY","VA");
    setupCallList(CALL_SET);
  }
  
  @Override
  public String getFilter() {
    return "MAILBOX@ccgvn.net";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!super.parseMsg(subject, body, data)) return false;
    
    // If no city was found, see if we can get it from the address line
    if (data.strCity.length() == 0) {
      
      // ALT may not be picked off end of address line because it looks like
      // part of the street name :(
      if (data.strAddress.endsWith(" ALT")) {
        data.strCity = "ALTAVISTA";
        data.strAddress = data.strAddress.substring(0,data.strAddress.length()-4).trim();
      }
      
      // Otherwise look for a dash separator
      else {
        int pt = data.strAddress.lastIndexOf("- ");
        if (pt >= 0) {
          data.strCity = data.strAddress.substring(pt+2).trim();
          data.strAddress = data.strAddress.substring(0,pt).trim();
        }
      }
    }
    
    return true;
  }

  private static final CodeSet CALL_SET = new CodeSet(
      "(3)ABDOMINAL DISTRESS",
      "(3)ACCIDENT",
      "ACCIDENT / VEHICLE",
      "ALARM / FIRE",
      "ALARM/FIRE",
      "ALLERGIC REACTION",
      "ASSAULT INJURIES",
      "ASSIST FIRE",
      "BACK INJURY",
      "BREATHING DIFFICULTY",
      "BRUSH/FIELD FIRE",
      "CARBON MONOXIDE INHALATION",
      "CARDIAC (WITH CARDIAC HISTORY)",
      "CHEST PAIN (NO CARDIAC HISTORY",
      "CHOKING",
      "DIABETIC ILLNESS/INSULIN REACT",
      "DIZZINESS/VERTIGO/WEAKNESS",
      "FALL/FRACTURE",
      "FIRST RESPONDER EMS CALL",
      "FUMES/GAS LEAK",
      "GUNSHOT WOUND",
      "HANGUP 911",
      "HEADACHE",
      "HEMMORHAGE/BLEEDING",
      "HYPERTENSION HIGH BLOOD PRESSU",
      "LACERATION",
      "MEDICAL ALARM",
      "MENTAL SUBJECT",
      "MUTUAL AID",
      "MUTUAL AID (OUTSIDE OF COUNTY",
      "MVC/MOTOR VEHICLE CRASH/ACCIDE",
      "OTHER/EXPLAIN IN COMMENTS",
      "OTHER - EXPLAIN IN REMARKS",
      "PAIN",
      "PSYCHIATRIC PATIENT",
      "SEIZURE/CONVULSIONS",
      "SICK (UNKNOWN MEDICAL)",
      "STABBING",
      "STANDBY",
      "STROKE",
      "TRANSPORT (DRS OFF TO HOSP)",
      "UNRESPONSIVE"
     );
  
  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "ALT", "ALTAVISTA",
      
      "EVI", "EVINGTON",
      "LYN", "LYNCH STATION",
      "RUS", "RUSTBURG"
  });
}
