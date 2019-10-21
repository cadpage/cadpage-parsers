package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class TXCherokeeCountyParser extends DispatchA55Parser {
  
  public TXCherokeeCountyParser() {
    super("CHEROKEE COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com";
  }

}
