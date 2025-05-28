package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXAustinCountyAParser extends DispatchA72Parser {

  public TXAustinCountyAParser() {
    super("AUSTIN COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "tpscad@austincountyso.org";
  }

}
