package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;



public class VALunenburgCountyParser extends DispatchDAPROParser {
  
  public VALunenburgCountyParser() {
    super("LUNENBURG COUNTY", "VA");
    setupCallList(CALL_SET);
  }
  
  @Override
  public String getFilter() {
    return "MAILBOX@rushpost.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strAddress = TRAILER_PK_PTN.matcher(data.strAddress).replaceAll("TRAILER PARK RD");
    if (data.strApt.startsWith("APTS")) {
      data.strAddress = append(data.strAddress, " ", "APTS");
      data.strApt = data.strApt.substring(4).trim();
    }
    return true;
  }
  private static final Pattern TRAILER_PK_PTN = Pattern.compile("\\bTRAILER PK(?: RD)?\\b", Pattern.CASE_INSENSITIVE);

  private static final CodeSet CALL_SET = new CodeSet(
      "ABDOMINAL PAIN",
      "ACCIDENT",
      "ALARM",
      "ALERGIC REACT",
      "ALLERGIC REACT",
      "ARGUEMENT",
      "ARGUMENT",
      "ASSIST",
      "ASTHMA",
      "BACK PAIN",
      "BLEEDING",
      "BRUSH FIRE",
      "CHEST PAINS",
      "DIABETIC",
      "DIVERSION CMHER - ICU DIVERSION",
      "DIFF BREATHING",
      "DOG BITE",
      "DUMPSTER FIRE",
      "DOA",
      "FALLEN",
      "FIRE",
      "FIRE ALARM",
      "HIGH BLOOD PRESS",
      "MUTUAL AID",
      "OTHER",
      "OTHER TRAINING",
      "PASSED OUT",
      "PREGNANCY",
      "PUBLIC SERVICE",
      "RESCUE",
      "SEIZURE",
      "SICK",
      "SMOKE IN BUILD",
      "STAND BY",
      "STROKE",
      "TREE IN ROAD",
      "UNRESPONSIVE"
  );
}
