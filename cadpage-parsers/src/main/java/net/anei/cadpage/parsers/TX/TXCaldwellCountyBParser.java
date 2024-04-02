package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class TXCaldwellCountyBParser extends DispatchA55Parser {

  public TXCaldwellCountyBParser() {
    super("CALDWELL COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com>";
  }

}
