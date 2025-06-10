package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class TXFreestoneCountyParser extends DispatchA55Parser {

  public TXFreestoneCountyParser() {
    super("FREESTONE COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }

}
