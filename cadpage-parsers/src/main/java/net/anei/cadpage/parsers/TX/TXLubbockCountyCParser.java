package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXLubbockCountyCParser extends DispatchA72Parser {
  
  public TXLubbockCountyCParser() {
    super("LUBBOCK COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "bgdickerson1@outlook.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    
    // Strip apt out of address
    String addr = data.strAddress;
    String apt = data.strApt;
    data.strAddress = data.strApt = "";
    parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr, data);
    data.strApt = append(apt, "-", data.strApt);
    return true;
  }
  
}
