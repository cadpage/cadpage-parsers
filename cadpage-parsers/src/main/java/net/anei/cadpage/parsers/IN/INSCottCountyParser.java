package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA80Parser;

public class INSCottCountyParser extends DispatchA80Parser {

  public INSCottCountyParser() {
    super("SCOTT COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "cad@scottcounty.in.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.contains(" - CALL:")) body = subject + '\n' + body;
    body = "DISPATCH:" + body;
    return super.parseMsg(body, data);
  }

}
