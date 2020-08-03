package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA60Parser;

public class SCWilliamsburgCountyParser extends DispatchA60Parser {

  public SCWilliamsburgCountyParser() {
    super("WILLIAMSBURG COUNTY", "SC");
  }

  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("911 Call")) return false;
    return super.parseMsg(body,  data);
  }

}
