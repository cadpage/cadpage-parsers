package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class COMontezumaCountyBParser extends DispatchA64Parser {

  public  COMontezumaCountyBParser() {
    super("MONTEZUMA COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "cadalerts@messaging.eforcesoftware.net,reports@messaging.eforcesoftware.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (body.contains("City State County:")) return false;
    return super.parseMsg(subject, body, data);
  }


}
