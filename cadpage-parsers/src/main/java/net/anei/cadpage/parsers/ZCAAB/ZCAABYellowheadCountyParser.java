package net.anei.cadpage.parsers.ZCAAB;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class ZCAABYellowheadCountyParser extends DispatchA74Parser {

  public ZCAABYellowheadCountyParser() {
    super("YELLOWHEAD COUNTY", "AB");
  }

  @Override
  public String getFilter() {
    return "911ecc@yhcounty.ca";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\nThe contents");
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = body.replace("VEGREVILLE/LAVOY RURAL", "VEGREVILLE");
    return super.parseMsg(subject, body, data);
  }
}
