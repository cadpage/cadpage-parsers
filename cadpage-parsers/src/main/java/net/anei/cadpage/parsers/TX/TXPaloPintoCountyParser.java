package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXPaloPintoCountyParser extends DispatchA72Parser {

  public TXPaloPintoCountyParser() {
    super("PALO PINTO COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "admin207804@co.palo-pinto.tx.us";
  }

}
