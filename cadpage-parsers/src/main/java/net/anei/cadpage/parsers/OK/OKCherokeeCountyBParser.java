package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchA38Parser;

public class OKCherokeeCountyBParser extends DispatchA38Parser {

  public OKCherokeeCountyBParser() {
    super("CHEROKEE COUNTY", "OK");
  }

  @Override
  public String getFilter() {
    return "911office@cherokeecounty-911.com";
  }

}
