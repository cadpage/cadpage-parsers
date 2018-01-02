package net.anei.cadpage.parsers.OK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class OKTulsaCParser extends SmartAddressParser {
  
  public OKTulsaCParser() {
    super("TULSA", "OK");
    setFieldList("CALL ADDR APT PLACE ID DATE TIME");
  }
  
  @Override
  public String getFilter() {
    return "pagealert@cityoftulsa.org";
  }
  
  private static final Pattern MASTER = Pattern.compile("CFS:(.*) -((?!\\s).*) -((?!\\s)\\S+) (\\d\\d?/\\d\\d/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d)");
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    parseAddress(StartType.START_ADDR, FLAG_ALLOW_DUAL_DIRECTIONS, match.group(2).trim(), data);
    data.strPlace = getLeft();
    data.strCallId = match.group(3);
    data.strDate = match.group(4);
    data.strTime = match.group(5);
    return true;
  }
}
