package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class VABuckinghamCountyBParser extends DispatchA71Parser {

  public VABuckinghamCountyBParser() {
    super("BUCKINGHAM COUNTY", "VA");
  }

  @Override
  public String getFilter() {
    return "notify@somahub.io";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.endsWith(" CLEARED")) data.msgType = MsgType.RUN_REPORT;
    return super.parseMsg(body, data);
  }

}
