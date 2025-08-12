package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class TXAustinCountyBParser extends DispatchA55Parser {

  public TXAustinCountyBParser() {
    super("AUSTIN COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "reports@messaging.eforcesoftware.net,ereports@eforcesoftware.com";
  }

}
