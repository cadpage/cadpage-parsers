package net.anei.cadpage.parsers.TN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

/**
 * Washington County, TN (B)
 */
public class TNWashingtonCountyBParser extends SmartAddressParser {
  
  private static final Pattern MASTER = Pattern.compile("(?:([A-Z]+)(?:\\.[^-]+)?-)?(.*?) +(?:AT|TO) +(.*?) (?:-X-ST:(.*?) +)?(?:MAP-([-,A-Z0-9 ]+?) +)?(\\d\\d:\\d\\d)");
  private static final Pattern SIGNAL_PTN = Pattern.compile("(.*?) +SIGNAL \\d");
  private static final Pattern HWY_11E_PTN = Pattern.compile("(HIGHWAY|HWY|US) +11 E\\b", Pattern.CASE_INSENSITIVE);
  
  public TNWashingtonCountyBParser() {
    super("WASHINGTON COUNTY", "TN");
    setFieldList("SRC CALL ADDR APT PLACE X MAP TIME");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    body = body.replace("(-1)", "");
    body = HWY_11E_PTN.matcher(body).replaceAll("$1 11E");
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    data.strSource = getOptGroup(match.group(1));
    data.strCall = match.group(2).trim();
    String addr = match.group(3).trim();
    data.strCross = getOptGroup(match.group(4)).replaceAll(" *- *", " & ");
    data.strMap = getOptGroup(match.group(5));
    data.strTime = match.group(6);
    
    match = SIGNAL_PTN.matcher(addr);
    if (match.matches()) addr = match.group(1);
    int pt = addr.indexOf(';');
    if (pt >= 0) {
      parseAddress(addr.substring(0,pt).trim(), data);
      data.strPlace = addr.substring(pt+1).trim();
    } else {
      parseAddress(StartType.START_ADDR, addr, data);
      data.strPlace = getLeft();
    }
    if (data.strApt.equals("0")) data.strApt = "";
    
    return true;
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    return sAddress.replace("OLD STATE ROUTE", "OLD_STATE_ROUTE");
  }

  @Override
  public String postAdjustMapAddress(String sAddress) {
    return sAddress.replace("OLD_STATE_ROUTE", "OLD STATE ROUTE");
  }
  
}
