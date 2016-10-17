package net.anei.cadpage.parsers.AK;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;

public class AKJuneauParser extends DispatchA3Parser {
  
  public AKJuneauParser() {
    super(0, "JUNEAU", "AK");
  }
  
  @Override
  public String getFilter() {
    return "";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("\n", "* ");
    return super.parseMsg(body, data);
  }
}
