package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NJMonmouthCountyGParser extends DispatchA19Parser {

  public NJMonmouthCountyGParser() {
    super("MONMOUTH COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Rule out NJMonmouthCountyD alerts
    if (subject.endsWith(" Notification")) return false;

    if (!super.parseMsg(subject, body, data)) return false;
    int pt = data.strCity.indexOf('(');
    if (pt >= 0) data.strCity = data.strCity.substring(0,pt).trim();
    return true;
  }

}
