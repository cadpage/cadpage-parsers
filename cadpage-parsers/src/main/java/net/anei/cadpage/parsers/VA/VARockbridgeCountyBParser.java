package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class VARockbridgeCountyBParser extends DispatchA57Parser {
  
  public VARockbridgeCountyBParser() {
    super("ROCKBRIDGE COUNTY", "VA");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@lexingtonva.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    if (!body.startsWith("Call Time:")) body = "Call Time:" + body;
    return super.parseMsg(body,  data);
  }
}
