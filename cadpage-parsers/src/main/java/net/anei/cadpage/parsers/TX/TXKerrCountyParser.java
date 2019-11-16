package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXKerrCountyParser extends DispatchA72Parser {
  
  public TXKerrCountyParser() {
    super("KERR COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "cad@co.kerr.tx.us";
  }
}
