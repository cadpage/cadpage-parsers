package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class TXCherokeeCountyAParser extends DispatchA55Parser {

  public TXCherokeeCountyAParser() {
    super("CHEROKEE COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com,ereports@eforcesoftware.com";
  }

}
