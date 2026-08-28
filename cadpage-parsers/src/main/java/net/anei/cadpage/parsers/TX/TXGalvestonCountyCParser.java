package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class TXGalvestonCountyCParser extends DispatchH03Parser {

  public TXGalvestonCountyCParser() {
    super("GALVESTON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "GCSO.GCSO@Premier-one.local";
  }

}
