package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;


public class NCNorthamptonCountyAParser extends DispatchA3Parser {

  public NCNorthamptonCountyAParser() {
    super(1, "NORTHAMPTON COUNTY", "NC");
  }

  @Override
  public String getFilter() {
    return "Northampton911@nhcnc.net,dispatcher@nhcnc.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Northampton911:");
    return super.parseMsg(body, data);
  }
}
