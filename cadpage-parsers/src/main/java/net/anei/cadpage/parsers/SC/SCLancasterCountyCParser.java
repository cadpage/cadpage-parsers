package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class SCLancasterCountyCParser extends MsgParser {
  
  public SCLancasterCountyCParser() {
    super("LANCASTER COUNTY", "SC");
    setFieldList("CALL ADDR APT CITY INFO");
  }
  
  private static final Pattern MASTER = Pattern.compile("(.*?)-In Progress-(.*)(?:\n(.*))?");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("DISPATCH")) return false;
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    String addr = match.group(2).trim();
    data.strSupp = getOptGroup(match.group(3));
    
    int pt = addr.lastIndexOf(',');
    if (pt >= 0) {
      data.strCity = addr.substring(pt+1).trim();
      addr = addr.substring(0,pt).trim();
    }
    addr = addr.replace('@', '&');
    parseAddress(addr, data);
    return true;
  }
}
