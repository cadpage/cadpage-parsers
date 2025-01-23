package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA60Parser;

/**
 * Floyd County, GA
 */
public class GAFloydCountyParser extends DispatchA60Parser {

  public GAFloydCountyParser() {
    super("FLOYD COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "no-reply@ledsportal.com,zuercher@floydcountyga.org,notification@floydcountyga.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Fire-related calls") &&
        !subject.equals("Call for Service")) return false;
    return super.parseMsg(body, data);
  }
}
