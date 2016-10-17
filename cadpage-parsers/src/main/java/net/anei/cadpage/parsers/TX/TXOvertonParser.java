package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA59Parser;

public class TXOvertonParser extends DispatchA59Parser {
  
  public TXOvertonParser() {
    super("OVERTON", "TX");
  }
  
  @Override
  public String getFilter() {
    return "overtonpd@ci.overton.tx.us";
  }
}
