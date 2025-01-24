package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class MIAlpenaCountyBParser extends DispatchProQAParser {

  public MIAlpenaCountyBParser() {
    super("ALPENA COUNTY", "MI",
          "ID! CALL CALL/SDS ADDR APT CITY! INFO/N+", true);
  }

  @Override
  public String getFilter() {
    return "ems.mail@midmichigan.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body, data);
  }

}
