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
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Parser p = new Parser(subject);
    data.strUnit = p.getLast(' ');
    String source = p.get();
    if (source.length() == 0) return false;
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