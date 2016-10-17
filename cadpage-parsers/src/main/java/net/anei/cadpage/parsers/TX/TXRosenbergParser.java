package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * Rosenberg, TX
 */
public class TXRosenbergParser extends DispatchA19Parser {
  
  public TXRosenbergParser() {
    super("ROSENBERG", "TX");
  }
  
  @Override
  public String getFilter() {
    return "@alert.active911.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " ETJ");
    return true;
  }
  
}
