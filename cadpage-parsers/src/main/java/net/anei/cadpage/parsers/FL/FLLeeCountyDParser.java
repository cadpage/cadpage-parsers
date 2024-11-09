package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.MsgParser;

public class FLLeeCountyDParser extends MsgParser {

  public FLLeeCountyDParser() {
    super("LEE COUNTY", "FL");
    setFieldList("ID CALL PLACE ADDR APT");
  }

  @Override
  public String getFilter() {
    return "cad@capecoral.gov";
  }

  private static final Pattern MASTER = Pattern.compile("Event (FD\\d{11}) \\((.*?)\\) at (?:(.*?) : )?(.*?) was (created|closed|reopened)\\.");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Active911 Notification")) return false;
    int pt = body.indexOf("\nMessage:");
    if (pt < 0) return false;
    body = body.substring(pt+9).trim();

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    data.strCall = match.group(2).trim();
    data.strPlace = getOptGroup(match.group(3));
    parseAddress(match.group(4).trim(), data);
    if (match.group(5).equals("closed")) data.msgType = MsgType.RUN_REPORT;
    return true;
  }

}
