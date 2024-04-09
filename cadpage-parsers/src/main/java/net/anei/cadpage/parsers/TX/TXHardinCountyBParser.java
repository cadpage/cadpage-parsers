package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA53Parser;

public class TXHardinCountyBParser extends DispatchA53Parser {

  public TXHardinCountyBParser() {
    super("HARDIN COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "dispatch@cityofsilsbee.com";
  }
}
