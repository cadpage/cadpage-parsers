package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;



public class VACarrollCountyParser extends DispatchDAPROParser {
  
  public VACarrollCountyParser() {
    super("CARROLL COUNTY", "VA");
    setupCallList(CALL_SET);
  }
  
  @Override
  public String getFilter() {
    return "MAILBOX@GalaxVa.com,pm_bounces@pm.mtasv.net";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("MAILBOX:")) return false;
    body = body.substring(8).trim();
    body = body.replace('\n', ' ');
    return super.parseMsg(body, data);
  }
  
  private static final CodeSet CALL_SET = new CodeSet(
      "EMS - BACK PAIN / INJURY",
      "EMS - BREATHING DIFFICULTY",
      "EMS - DIABETIC",
      "EMS - NAUSEA / VOMITING",
      "EMS - PAIN",
      "FIRE - ELECTRICAL",
      "FIRE - VEHICLE"
  );
}