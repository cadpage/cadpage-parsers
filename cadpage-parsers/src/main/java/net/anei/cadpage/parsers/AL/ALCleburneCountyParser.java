package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchBCParser;


public class ALCleburneCountyParser extends DispatchBCParser {
  public ALCleburneCountyParser() {
    super("CLEBURNE COUNTY", "AL");
  }
   
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (subject.equals("Run Report")) data.msgType = MsgType.RUN_REPORT;
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public String getFilter() {
    return "911@CLEBURNECOUNTY.US";
  }
}
