package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA82Parser;

public class TXCollinCountyCParser extends DispatchA82Parser {

  public TXCollinCountyCParser() {
    this("COLLIN COUNTY", "TX");
  }

  protected TXCollinCountyCParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getFilter() {
    return "icsuser@co.collin.tx.us,ccsodispatch@co.collin.tx.us,inforad@co.walker.tx.us";
  }

}