package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchCiscoParser;

/**
 * Howard County, IN
 */
public class INHowardCountyAParser extends DispatchCiscoParser {
  
  public INHowardCountyAParser() {
    super("HOWARD COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "cisco.paging@co.howard.in.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    
    // Reparse address looking for trailing apt
    if (data.strApt.length() == 0) {
      String addr = data.strAddress;
      data.strAddress = "";
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr, data);
    }
    return true;
  }
  
}
