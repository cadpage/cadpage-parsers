package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;


public class SCLancasterCountyAParser extends DispatchA57Parser {
  
  public SCLancasterCountyAParser() {
    super("LANCASTER COUNTY", "SC");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@lanc911.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Incident Notification")) return false;
    return super.parseMsg(body, data);
  };
}
