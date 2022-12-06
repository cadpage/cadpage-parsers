package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class TXEastlandCountyParser extends DispatchA64Parser {

  public TXEastlandCountyParser() {
    super("EASTLAND COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }
}
