package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class TXOvertonParser extends DispatchA64Parser {
  
  public TXOvertonParser() {
    super("OVERTON", "TX");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com";
  }
}
