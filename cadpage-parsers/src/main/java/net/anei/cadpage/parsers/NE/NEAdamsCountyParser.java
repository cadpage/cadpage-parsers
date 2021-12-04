package net.anei.cadpage.parsers.NE;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA38Parser;

public class NEAdamsCountyParser extends DispatchA38Parser {

  public NEAdamsCountyParser() {
    super("ADAMS COUNTY", "NE");
  }

  @Override
  public String getFilter() {
    return "TAC10Email@hastingspolice.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\nThe information contained ");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body, data);
  }
}
