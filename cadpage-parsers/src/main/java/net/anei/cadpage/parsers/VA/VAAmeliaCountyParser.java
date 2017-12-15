package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;

/**
 * Amelia County, VA
 */
public class VAAmeliaCountyParser extends DispatchDAPROParser {
  
  public VAAmeliaCountyParser() {
    super(CITY_CODE_TABLE, "AMELIA COUNTY", "VA");
    setupCallList(CALL_SET);
  }
  
  @Override
  public String getFilter() {
    return "MAILBOX@ameliasheriff.org";
  }
  
  private static final CodeSet CALL_SET = new CodeSet(
    "ABDOMINAL PAIN / PROBLEMS",
    "AIRCRAFT EMERGENCY",
    "ALARM",
    "ALLERGIES / ENVENOMATIONS",
    "BACK PAIN",
    "BREATHING PROBLEMS",
    "CARDIAC / RESPIRATORY ARREST",
    "CHEST PAIN",
    "HEADACHE",
    "HEMORRHAGE / LACERATIONS",
    "OUTSIDE FIRE",
    "SICK PERSON",
    "SPECIAL ASSIGNMENT",
    "STRUCTURE FIRE",
    "TRAFFIC/TRANSPORTATION ACCIDEN",
    "TRAFFIC VIOLATION/COMPLAINT/HA",
    "TRAUMATIC INJURIES",
    "UNCONSCIOUS / FAINTING",
    "UNKNOWN PROBLEM / MAN DOWN"
  );
  
  private static final Properties CITY_CODE_TABLE =
    buildCodeTable(new String[]{
        "AME", "AMELIA COURT HOUSE",
        "CHU", "CHULA"
    });
}