package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class TXRockwallCountyDParser extends DispatchA57Parser {

  public TXRockwallCountyDParser() {
    super("ROCKWALL COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "donotreply@rockwallcountytexas.com";
  }

}
