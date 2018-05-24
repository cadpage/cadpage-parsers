package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class PABerksCountyBParser extends DispatchA57Parser {
  
  public PABerksCountyBParser() {
    super("BERKS COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "berksalert@countyofberks.com,99538";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Berks County DES:");
    return super.parseMsg(body, data);
  }

}
