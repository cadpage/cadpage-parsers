package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;



public class KYKentonCountyAParser extends DispatchA57Parser {

  public KYKentonCountyAParser() {
    super("KENTON COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@kentoncounty.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("From KCECC")) return false;
    return super.parseMsg(body, data);
  }
}
