package net.anei.cadpage.parsers.CT;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class CTMadisonParser extends SmartAddressParser {
  
  private static final Pattern MASTER = 
      Pattern.compile("Madison: (.*?) +X-STR:(?: +-)? +(.*?) +Premise: 0*(.*)");
  private static final Pattern CODE_PTN = Pattern.compile("- (\\d{1,2}-[A-Z]-\\d) +0*|  +0*| 0+");
  
  public CTMadisonParser() {
    super("MADISON", "CT");
    setFieldList("CALL CODE ADDR APT X PLACE");
  }

  @Override
  public String getFilter() {
    return "mdsn911@gmail.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    String sAddr = match.group(1).trim();
    String cross = match.group(2).trim();
    if (cross.startsWith("/")) cross = cross.substring(1).trim();
    String place = match.group(3).trim();
    
    match = CODE_PTN.matcher(sAddr);
    if (match.find()) {
      data.strCall = sAddr.substring(0,match.start()).trim();
      data.strCode = getOptGroup(match.group(1));
      sAddr = sAddr.substring(match.end()).trim();
      parseAddress(sAddr.replaceAll("  +", " "), data);
    } else {
      parseAddress(StartType.START_CALL, FLAG_ANCHOR_END, sAddr, data);
      sAddr = data.strAddress.replace(" & ", "/ ");
    }
    data.strCross = cross;
    sAddr = sAddr.replace(" ", "");
    if (!place.replace(" ", "").equalsIgnoreCase(sAddr)) data.strPlace = place;
    return true;
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    int pt = sAddress.indexOf('(');
    if (pt >= 0) sAddress = sAddress.substring(0,pt).trim();
    return sAddress;
  }
  
}
