package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class MIEatonCountyBParser extends MsgParser {

  public MIEatonCountyBParser() {
    super("EATON COUNTY", "MI");
    setFieldList("SRC CALL ADDR APT CITY ID");
  }

  @Override
  public String getFilter() {
    return "MI-Eaton-County-EMA@email.getrave.com";
  }

  private static final Pattern MASTER = Pattern.compile("Eaton Critical Alert: +(\\S+) Admin Notification. +(.*?) has occurred at +([^,]*), *([A-Z ]*)\n+IncidentID:  *(\\d+)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Eaton Critical Alert:")) return false;
    int pt = body.indexOf("\n\nClick the");
    if (pt >= 0) body = body.substring(0,pt).trim();

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strSource = match.group(1);
    data.strCall = match.group(2).trim();
    parseAddress(match.group(3).trim(), data);
    data.strCity = match.group(4).trim();
    data.strCallId = match.group(5);
    return true;
  }
}
