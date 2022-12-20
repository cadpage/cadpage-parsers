package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;

public class SCAikenCountyParser extends MsgParser {

  public SCAikenCountyParser() {
    super("AIKEN COUNTY", "SC");
    setFieldList("ADDR APT");
  }

  @Override
  public String getFilter() {
    return "CAD@cityofaikensc.gov";
  }

  private static final Pattern MASTER = Pattern.compile("(?:FYI|Update): *;([^;\n]+)");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("TxtAlrt")) return false;
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0, pt).trim();
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    parseAddress(match.group(1).trim(), data);
    return true;
  }

}
