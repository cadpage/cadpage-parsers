package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYFloydCountyBParser extends DispatchA27Parser {

  public KYFloydCountyBParser() {
    super("FLOYD COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "prestonsburgky@cissystem.com";
  }
}
