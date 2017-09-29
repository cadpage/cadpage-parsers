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
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Parser p = new Parser(subject);
    data.strUnit = p.getLast(' ');
    String source = p.get();
    if (!super.parseMsg(body,  data)) return false;
    data.strBox = data.strSource;
    data.strSource = source;
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC UNIT BOX " + super.getProgram();
  }
  
  private static final CodeSet CALL_SET = new CodeSet(
      "ABDOMINAL PAIN",
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