package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;

/**
 * Greene County, VA
 */
public class VAGreeneCountyParser extends DispatchDAPROParser {
  
  public VAGreeneCountyParser() {
    super("GREENE COUNTY", "VA");  
    setupCallList(CALL_SET);
    setupMultiWordStreets("SOUTH RIVER");
  }
  
  @Override
  public String getFilter() {
    return "MAILBOX@gcvasheriff.us ";
  }
  
  private static final CodeSet CALL_SET = new CodeSet(
      "ALARM ACTIVATION",
      "ALLERGIC REACTION",
      "ANIMAL",
      "BREATHING DIFFICULTY",
      "BRUSH FIRE",
      "CHEST PAIN/HEART PROBLEMS",
      "CHOKING",
      "CPR-ADULT(UNWITNESSED)",
      "DIABETIC",
      "FALLS/ACCIDENTS",
      "INVESTIGATION/UNKNOWN TYPE",
      "LANDING ZONE SET UP",
      "MEDICAL ASSIST",
      "MOTOR VEHICLE ACCIDENT",
      "PUBLIC SERVICE",
      "SICK/UNKNOWN",
      "SMOKE INSIDE STRUCTURE",
      "STRUCTURE FIRE",
      "TRAFFIC - ACCIDENT",
      "TREE IN THE ROADWAY",
      "OUTSIDE SMOKE INVESTIGATION",
      "UNAUTHORIZED BURNING",
      "UNCONSCIOUS/SYNCOPE"
  );
}