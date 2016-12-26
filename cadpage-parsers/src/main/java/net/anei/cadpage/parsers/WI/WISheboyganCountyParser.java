package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;



public class WISheboyganCountyParser extends DispatchA19Parser {
  
  public WISheboyganCountyParser() {
    super("SHEBOYGAN COUNTY", "WI");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strAddress.startsWith(data.strPlace)) data.strPlace = "";
    return true;
  }
  
}
