package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class MOPerryCountyCParser extends DispatchA24Parser {

  public MOPerryCountyCParser() {
    super("PERRY COUNTY", "MO");
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("DISPATCH:")) return false;
    body = body.substring(9).trim();
    return super.parseMsg(body, data);
  }
}
