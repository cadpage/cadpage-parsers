package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class NYSullivanCountyBParser extends DispatchA57Parser {
  
  public NYSullivanCountyBParser() {
    super("SULLIVAN COUNTY", "NY");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@wcpa911.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    return super.parseMsg(body, data);
  }

}
