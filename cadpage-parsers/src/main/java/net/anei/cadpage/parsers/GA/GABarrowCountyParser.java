package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class GABarrowCountyParser extends DispatchA57Parser {
  
  public GABarrowCountyParser() {
    super("BARROW COUNTY", "GA");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("Dispatch:")) return false;
    body = body.substring(9);
    return super.parseMsg(body, data);
  }
}
