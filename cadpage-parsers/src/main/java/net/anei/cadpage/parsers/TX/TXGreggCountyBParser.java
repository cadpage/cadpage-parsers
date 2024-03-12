package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class TXGreggCountyBParser extends DispatchA55Parser {

  public TXGreggCountyBParser() {
    super("GREGG COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }

}
