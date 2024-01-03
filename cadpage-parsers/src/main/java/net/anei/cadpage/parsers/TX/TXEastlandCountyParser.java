package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class TXEastlandCountyParser extends DispatchA55Parser {

  public TXEastlandCountyParser() {
    super("EASTLAND COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }
}
