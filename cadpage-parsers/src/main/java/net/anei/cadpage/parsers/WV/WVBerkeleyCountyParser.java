package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Berkeley County, WV
 */
public class WVBerkeleyCountyParser extends MsgParser {

  public WVBerkeleyCountyParser() {
    super("BERKELEY COUNTY", "WV");
    setFieldList("ADDR APT CODE CALL");
  }

  @Override
  public String getFilter() {
    return "alerts@berkeleywv.org";
  }

  private static final Pattern MASTER1 = Pattern.compile("Address(.*?) Title(?:(\\d\\d) +)?(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Probably transient new format
    Matcher match = MASTER1.matcher(body);
    if (!match.matches()) return false;
    parseAddress(match.group(1).trim(), data);
    data.strCode = getOptGroup(match.group(2));
    data.strCall = match.group(3);
    return true;
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = PAREN_PTN.matcher(addr).replaceFirst("").trim();
    return addr;
  }
  private static final Pattern PAREN_PTN = Pattern.compile("\\(.*\\)$");
}
