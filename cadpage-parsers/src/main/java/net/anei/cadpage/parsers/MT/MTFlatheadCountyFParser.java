package net.anei.cadpage.parsers.MT;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchC07Parser;

public class MTFlatheadCountyFParser extends DispatchC07Parser {

  public MTFlatheadCountyFParser() {
    super("FLATHEAD COUNTY", "MT");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    int pt = data.strAddress.indexOf(',');
    if (pt >= 0) {
      String part1 = data.strAddress.substring(0,pt).trim();
      String part2 = data.strAddress.substring(pt+1).trim();
      if (part1.equals(part2)) data.strAddress = part1;
    }
    return true;
  }
}
