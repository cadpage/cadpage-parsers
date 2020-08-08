package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class IDIdahoCountyParser extends DispatchA19Parser {

  public IDIdahoCountyParser() {
    super("IDAHO COUNTY", "ID");
  }

  @Override
  public String getFilter() {
    return "spillmannotify@idahocounty.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;

    // City code is just not reliable
    data.strCity = "";
    return true;
  }

}
