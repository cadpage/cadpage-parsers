package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA87Parser;

public class WAClallamCountyBParser extends DispatchA87Parser {

  public WAClallamCountyBParser() {
    super("CLALLAM COUNTY", "WA");
    removeWords("LA");
  }

  @Override
  public String getFilter() {
    return "Dispatch@co.clallam.wa.us,Dispatch@clallamcountywa.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Incident")) return false;

    return super.parseMsg(body, data);
  }
}
