package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class ALDallasCountyParser extends SmartAddressParser {
  
  public ALDallasCountyParser() {
    super("DALLAS COUNTY", "AL");
    setFieldList("CODE CALL ADDR APT X PLACE");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    if (!body.startsWith("DALLAS911:")) return false;
    body = body.substring(10).trim();
    
    int pt = subject.indexOf(' ');
    if (pt < 0) return false;
    data.strCode = subject.substring(0,pt);
    data.strCall = subject.substring(pt+1).trim();
    
    parseAddress(StartType.START_ADDR, FLAG_CROSS_FOLLOWS, body, data);
    body = stripFieldStart(getLeft(), "Bldg");
    parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, body, data);
    data.strPlace = cleanWirelessCarrier(getLeft());
    return true;
  }

}
