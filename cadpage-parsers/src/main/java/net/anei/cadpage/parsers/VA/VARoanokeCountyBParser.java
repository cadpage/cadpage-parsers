package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA95Parser;

public class VARoanokeCountyBParser extends DispatchA95Parser {

  public VARoanokeCountyBParser() {
    super("ROANOKE COUNTY", "VA");
  }

  @Override
  public String getFilter() {
    return "noreply-centralsquare@salemva.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\n_____");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }

}
