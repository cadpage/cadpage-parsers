package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class COLasAnimasCountyAParser extends DispatchA55Parser {

  public COLasAnimasCountyAParser() {
    super("LAS ANIMAS COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "reports@messaging.eforcesoftware.net,ereports@eforcesoftware.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Alert")) return false;
    return super.parseMsg("", body, data);
  }

}
