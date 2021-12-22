package net.anei.cadpage.parsers.ZCABC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA51Parser;

public class ZCABCCentralOkanaganParser extends DispatchA51Parser {

  public ZCABCCentralOkanaganParser() {
    super("", "BC");
  }

  @Override
  public String getLocName() {
    return "Central Okanagan";
  }

  @Override
  public String getFilter() {
    return "equipment@rdkb.com,no-reply@kelowna.ca";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Incident Message")) return false;
    return super.parseMsg(body, data);
  }
  
}
