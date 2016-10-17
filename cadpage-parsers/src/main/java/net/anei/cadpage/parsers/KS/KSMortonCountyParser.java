package net.anei.cadpage.parsers.KS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class KSMortonCountyParser extends SmartAddressParser {

  private static final Pattern MASTER = Pattern.compile("((?:[A-Z0-9]*[0-9][A-Z0-9]* )+)(.*?)(?: - ([A-Z]+ SECTOR))?");

  public KSMortonCountyParser() {
    super("MORTON COUNTY", "KS");
    setFieldList("UNIT CALL ADDR APT MAP");
  }
  
  @Override
  public String getFilter() {
    return "global@elkhart.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strUnit = match.group(1).trim();
    String addr = match.group(2).trim();
    data.strMap = getOptGroup(match.group(3));
    
    int pt = addr.lastIndexOf(')');
    if (pt >= 0) {
      data.strCall = addr.substring(0,pt).trim();
      parseAddress(addr.substring(pt+1).trim(), data);
    } else {
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, addr, data);
    }
    return true;
  }
}
