package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class TXHoustonCountyAParser extends DispatchA64Parser {

  public TXHoustonCountyAParser() {
    super("HOUSTON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com,ereports@eforcesoftware.com";
  }
}
