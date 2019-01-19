package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class FLCocoaParser extends DispatchA57Parser {

  public FLCocoaParser() {
    super("COCOA", "FL");
  }
  
  @Override
  public String getFilter() {
    return "DispatchDoNotReply@cocoafl.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Information")) return false;
    return super.parseMsg(body, data);
  }
}
