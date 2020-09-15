package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class ALHooverBParser extends DispatchA57Parser {

  public ALHooverBParser() {
    super("HOOVER", "AL");
  }
  
  @Override
  public String getFilter() {
    return "dispatchNWPS@ci.hoover.al.us";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!CAD ALERT!")) return false;
    return super.parseMsg(body, data);
  }
}
