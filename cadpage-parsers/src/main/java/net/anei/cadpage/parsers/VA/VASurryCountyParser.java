package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;

/**
 * Surry County, VA
 */
public class VASurryCountyParser extends DispatchDAPROParser {
  
  public VASurryCountyParser() {
    super(CITY_CODE_TABLE, "SURRY COUNTY", "VA");
    setupCallList(CALL_SET);
  }
  
  @Override
  public String getFilter() {
    return "mailbox@sheriffsoffice.local";
  }
  
  private static final CodeSet CALL_SET = new CodeSet(
      "ABDOMINAL PAIN",
      "ALTERED MENTAL STATUS",
      "CHEST PAIN/HEART ATTACK",
      "CITIZEN ASSIST",
      "DIFFICULTY BREATHING",
      "FALL",
      "MVA- MOTOR VEHICLE ACCIDENT",
      "MVA W/ INJURIES",
      "ODOR OF SMOKE IN STRUCTURE",
      "OTHER/UNKNOWN PROBLEM",
      "PAIN",
      "SICK",
      "STRUCTURE FIRE",
      "TEST CALL",
      "UNCONSCIOUS/UNRESPONSIVE"
  );
  
  private static final Properties CITY_CODE_TABLE =
    buildCodeTable(new String[]{
        "DEN", "DENDRON",
        "ELB", "ELBERON",
        "SPR", "SPRING GROVE",
        "SUR", "SURRY",
        "WAV", "WAVERLY"
    });
}