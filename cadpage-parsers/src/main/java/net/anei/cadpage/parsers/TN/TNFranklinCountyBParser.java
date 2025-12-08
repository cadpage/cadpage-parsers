package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchC04Parser;

public class TNFranklinCountyBParser extends DispatchC04Parser {

  public TNFranklinCountyBParser() {
    super("FRANKLIN COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "noreply@tnsewaneepd.mmmicro.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\nThis message is");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }
}
