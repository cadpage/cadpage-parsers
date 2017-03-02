package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class TXHendersonCountyParser extends DispatchA64Parser {
  
  public TXHendersonCountyParser() {
    super("HENDERSON COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com";
  }

}
