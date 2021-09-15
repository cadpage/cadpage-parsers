package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;

public class VAPrinceEdwardCountyBParser extends MsgParser {

  public VAPrinceEdwardCountyBParser() {
    super("PRINCE EDWARD COUNTY", "VA");
    setFieldList("CALL ID ADDR APT UNIT X INFO");
  }

  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com,no-reply@ledsportal.com";
  }

  private static final Pattern LEAD_ID_PTN = Pattern.compile("(FECC\\d{2}-\\d{5}) +");
  private static final Pattern UNIT_PTN = Pattern.compile(" *\\b((?:(?:[A-Z]+\\d+|\\d{2}|MRS|PER)\\b[; ]*)+)\\b(?: +|$)");
  private static final Pattern UNIT_DELIM_PTN = Pattern.compile("[; ]+");
  private static final Pattern INFO_DELIM_PTN = Pattern.compile("[; ]+\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    data.strCall = subject;

    body = stripFieldEnd(body, " None");
    body = stripFieldEnd(body, " None");

    Matcher match = LEAD_ID_PTN.matcher(body);
    if (!match.lookingAt()) return false;
    data.strCallId = match.group(1);
    body = body.substring(match.end());

    if (subject.length() > 0) {
      if (body.length() < subject.length()) return false;
      if (!subject.equals(body.substring(0, subject.length()))) return false;
      body = body.substring(subject.length());
      if (!body.startsWith(" ")) return false;
      body = body.trim();
    }

    match = UNIT_PTN.matcher(body);
    if (!match.find()) return false;
    parseAddress(body.substring(0,match.start()), data);
    data.strUnit = UNIT_DELIM_PTN.matcher(match.group(1).trim()).replaceAll(",");
    body = body.substring(match.end());

    String[] parts = INFO_DELIM_PTN.split(body);
    if (!parts[0].equals("None")) data.strCross = parts[0];
    for (int ndx = 1; ndx < parts.length; ndx++) {
      data.strSupp = append(data.strSupp, "\n", parts[ndx]);
    }
    return true;
  }

}
