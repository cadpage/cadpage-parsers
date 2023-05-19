package net.anei.cadpage.parsers.MI;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Clinton County, MI
 */
public class MIClintonCountyParser extends DispatchOSSIParser {

  public MIClintonCountyParser() {
    super("CLINTON COUNTY", "MI",
          "( CANCEL ADDR! SKIP " +
          "| FYI? ( CALL ADDR X+? " +
                 "| ADDR X/Z+? CALL! PRI? " +
                 ") " +
          ") INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CAD@clinton-county.org,CAD@shiawassee.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("PRI")) return new PriorityField("\\d", true);
    return super.getField(name);
  }

  private static final Pattern CALL_PTN = Pattern.compile(
             ".* [1-4]|" +
             "ACCIDENT WITH UNKNOWN INJURY|" +
             "ALARM FIRE|" +
             "CARBON MONOXIDE.*|" +
             "CHECK WELFARE|" +
             "COMMUNITY POLICING|" +
             "DEAD ON ARRIVAL|" +
             "EMERGENCY SERVICES TEAM|" +
             "FIRE .*|" +
             "GAS LEAK|" +
             "HAZARDOUS MATERIALS|" +
             "HIT AND RUN PI ACCIDENT|" +
             "HOSPITAL TRANSFER|" +
             "INCIDENT MANAGEMENT TEAM|" +
             "LIFT ASSIST.*|" +
             "MEDICAL.*|" +
             "MOUNTED DIVISION|" +
             "MUTUAL AID.*|" +
             "PERSONAL INJURY.*|" +
             "POWER LINES DOWN|" +
             "PRIVATE CALL|" +
             "PSYCHIATRIC.*|" +
             "ROAD CLOSED|" +
             "SPECIAL OPERATIONS TEAM|" +
             "STRUCTURE FIRE|" +
             "SUICIDAL SUBJECT|" +
             "SUICIDE.*|" +
             "TRAINING.*|" +
             "UNKNOWN ACCIDENT");
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    public boolean checkParse(String field, Data data) {
      if (!getRelativeField(+1).equals("1") &&
          !CALL_PTN.matcher(field).matches()) return false;
      super.parse(field, data);
      return true;
    }

    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern BUS127_PTN = Pattern.compile("\\bBUS *127", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String addr) {
    addr = BUS127_PTN.matcher(addr).replaceAll("OLD US 27");
    return addr;
  }
}
