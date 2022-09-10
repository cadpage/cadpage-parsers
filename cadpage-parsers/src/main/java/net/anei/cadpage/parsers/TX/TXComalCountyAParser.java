package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXComalCountyAParser extends DispatchA72Parser {
  
  public TXComalCountyAParser() {
    super("COMAL COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "ccsocad@co.comal.tx.us";
  }
}
