package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class TXDallasCountyIParser extends DispatchA55Parser {

  public TXDallasCountyIParser() {
    super("DALLAS COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }

}
