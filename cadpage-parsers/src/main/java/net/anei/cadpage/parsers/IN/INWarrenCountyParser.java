package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA80Parser;

public class INWarrenCountyParser extends DispatchA80Parser {

  public INWarrenCountyParser() {
    super("WARREN COUNTY", "IN");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("DISPATCH:")) body = "DISPATCH:" + body;
    return super.parseMsg(body, data);
  }
}
