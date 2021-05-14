package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class TXHendersonCountyAParser extends DispatchA64Parser {
  
  public TXHendersonCountyAParser() {
    super("HENDERSON COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com";
  }

}
