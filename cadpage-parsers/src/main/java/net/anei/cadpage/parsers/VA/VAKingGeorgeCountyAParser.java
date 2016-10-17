package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;



public class VAKingGeorgeCountyAParser extends DispatchDAPROParser {

  public VAKingGeorgeCountyAParser() {
    super(CITY_LIST, "KING GEORGE COUNTY", "VA");
    setupCallList(CALL_SET);
  }
  
  @Override
  public String getFilter() {
    return "CAD@co.kinggeorge.state.va.us,14100";
  }

  private static final CodeSet CALL_SET = new CodeSet(
      "ABDOMINAL PAIN",
      "ALLERGIC REACTION",
      "ASTHMA ATTACK",
      "CHEST PAIN",
      "CHOKING",
      "DIABETIC EMERGENCY",
      "DIFFICULTY BREATHING",
      "FIRE ALARM RESIDENTIAL",
      "ILLNESS",
      "INJURIES FROM A FALL",
      "LACERATION",
      "LOCKED OUT OF CAR OR RESIDENCE",
      "MEDICAL ALARM",
      "MOTOR VEHICLE ACCIDENT",
      "MUTUAL AID TO (FIRE)",
      "MUTUAL AID TO (RESCUE)",
      "MVA OVERTURNED",
      "OVERDOSE",
      "PUBLIC SERVICE - FIRE DEPT",
      "PUBLIC SERVICE - RESCUE SQUAD",
      "SEIZURES",
      "SPECIAL PROGRAM",
      "SUICIDE (ACTUAL OR ATTEMPT)",
      "TRAFFIC STOP",
      "UNRESPONSIVE",
      "WALK IN PATIENT"
  );

  private static final String[] CITY_LIST = new String[]{
    "PORT ROYAL"
  };
}