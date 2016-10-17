package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;



public class WAKingCountyBParser extends MsgParser {
  
  private static final Pattern MASTER = Pattern.compile("CAD\\|\\|(?:(.*?) # (.*?) (FTAC\\d) +(.*)|Addr: *(.*?) Problem: *(.*?) Emergency Units: *([A-Z0-9,]+)(?:, \\[.*)?)");
  
  public WAKingCountyBParser() {
    super("KING COUNTY", "WA");
    setFieldList("ADDR APT CALL CH UNIT");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    String addr = match.group(1);
    if (addr != null) {
      parseAddress(addr.trim(), data);
      data.strCall = match.group(2).trim();
      data.strChannel = match.group(3);
      data.strUnit = match.group(4);
    } else {
      parseAddress(match.group(5).trim(), data);
      data.strCall = match.group(6).trim();
      data.strUnit = match.group(7);
    }
    return true;
  }
}
