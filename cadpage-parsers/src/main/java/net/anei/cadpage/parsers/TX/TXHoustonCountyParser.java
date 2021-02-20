package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class TXHoustonCountyParser extends DispatchA64Parser {

  public TXHoustonCountyParser() {
    super("HOUSTON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com";
  }
}
