package net.anei.cadpage.parsers.ZCAON;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA91Parser;

public class ZCAONSimcoeCountyCParser extends DispatchA91Parser {

  public ZCAONSimcoeCountyCParser() {
    super("SIMCOE COUNTY", "ON");
  }

  @Override
  public String getFilter() {
    return "Paging@yrp.ca";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\n--");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }

}
