package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;


public class VANewKentCountyParser extends DispatchDAPROParser {
  
  public VANewKentCountyParser() {
    super("NEW KENT COUNTY", "VA");
    setupCallList(CALL_SET);
  }
  
  @Override
  public String getFilter() {
    return "MAILBOX@rushpost.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strApt.equals("0")) data.strApt = "";
    if (data.strAddress.endsWith(" 0")) {
      data.strAddress = data.strAddress.substring(0,data.strAddress.length()-2).trim();
    }
    return true;
  }
  
  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_RECHECK_APT;
  }
  
  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.equals("0")) return true;
    return super.isNotExtraApt(apt);
  }
  
  private static final CodeSet CALL_SET = new CodeSet(
      "ASSIST ANOTHER AGENCY",
      "BREATHING DIFFICULTY",
      "CARDIAC (WITH HISTORY)",
      "DEBRIS/TREE IN ROAD",
      "MOTOR VEHICLE ACCIDENT",
      "RESIDENTIAL FIRE ALARM",
      "SEIZURE/CONVULSIONS",
      "SEIZURE/CONVUSIONS",
      "SICK (UNKNOWN MEDICAL)",
      "SUSPICIOUS SITUATION",
      "TEST ONLY",
      "VEHICLE FIRE"
  );
}
