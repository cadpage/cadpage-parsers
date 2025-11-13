package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


/**
 * Warren County, NJ
 */
public class NJWarrenCountyParser extends SmartAddressParser {

  public NJWarrenCountyParser() {
    super("WARREN COUNTY", "NJ");
    setFieldList("SRC CALL UNIT PLACE ADDR APT X ID GPS INFO");
  }

  @Override
  public String getFilter() {
    return "WC911@co.warren.nj.us,root@co.warren.nj.us";
  }

  private static final Pattern MASTER = Pattern.compile("([A-Z0-9]+)(?: ALERT)?: (.*?) \\(([A-Z0-9]+) *\\) *([^,]*?),(?: NEAR: ([^:]*?):)?([^:]*) (\\d{8})(?: :: (.*))?");
  private static final Pattern GPS_PTN = Pattern.compile("([-+]?\\d{2}\\.\\d{6} [-+]?\\d{2}\\.\\d{6})\\b[/ ]*");

  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strSource =  match.group(1);
    data.strCall = match.group(2).trim();
    data.strUnit = match.group(3);
    String addr = match.group(4).trim();
    int pt = addr.indexOf('/');
    if (pt >= 0) {
      data.strPlace = addr.substring(0,pt).trim();
      addr = addr.substring(pt+1).trim();
    }
    parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr, data);
    data.strCross = getOptGroup(match.group(5));
    data.strCall = append(data.strCall, " - ", match.group(6).trim());
    data.strCallId = match.group(7);
    String info = getOptGroup(match.group(8));
    match  = GPS_PTN.matcher(info);
    if (match.lookingAt()) {
      setGPSLoc(match.group(1), data);
      info = info.substring(match.end());
    }
    data.strSupp = info;
    return true;
  }
}
