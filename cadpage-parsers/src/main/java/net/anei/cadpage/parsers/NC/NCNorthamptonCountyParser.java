package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;


public class NCNorthamptonCountyParser extends DispatchA3Parser {

  private static final String PREFIX = "Northampton911:*";

  public NCNorthamptonCountyParser() {
    super(1, PREFIX, "NORTHAMPTON COUNTY", "NC");
  }

  @Override
  public String getFilter() {
    return "Northampton911@nhcnc.net,dispatcher@nhcnc.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("INTERNAL") && ! body.startsWith(PREFIX)) body = PREFIX + body;
    return super.parseMsg(body, data);
  }
}
