package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;



public class VACarolineCountyBParser extends SmartAddressParser {
  
  public VACarolineCountyBParser() {
    super("CAROLINE COUNTY", "VA");
    setFieldList("CODE CALL ADDR APT X");
    setupCallList(CALL_LIST);
  }
  
  private static final Pattern MASTER = Pattern.compile("911-CENTER:([-A-Z0-9]+) +(.*)");
  private static final Pattern APT_BLDG_PTN = Pattern.compile("(.*?) Apt: *(.*?) +Bldg\\b(?: +(\\d+)\\b)? *(.*?)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    data.strCode = match.group(1);
    String addr = match.group(2);
    
    match = APT_BLDG_PTN.matcher(addr);
    if (match.matches()) {
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, match.group(1).trim(), data);
      String apt = match.group(2);
      String bldg = match.group(3);
      if (bldg != null) apt = append(apt, " ", "Bldg " + bldg);
      data.strApt = append(data.strApt, "-", apt);
      data.strCross = match.group(4);
    } else {
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_CROSS_FOLLOWS, addr, data);
      data.strCross = getLeft();
    }
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "B&E/BURGLARY",
      "CHECK POINT/DETAIL",
      "DOMESTIC",
      "PURSUIT",
      "STAB/GUNSHOT/PENETRATING WOUND"
  );
}
