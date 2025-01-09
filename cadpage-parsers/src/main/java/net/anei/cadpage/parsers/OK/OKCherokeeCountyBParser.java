package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class OKCherokeeCountyBParser extends DispatchA19Parser {

  public OKCherokeeCountyBParser() {
    super("CHEROKEE COUNTY", "OK");
  }

  @Override
  public String getFilter() {
    return "911office@cherokeecounty-911.com";
  }

}
