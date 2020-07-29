package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class TXHaysCountyDParser extends DispatchA57Parser {

  public TXHaysCountyDParser() {
    super("HAYS COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "cadpage@co.hays.tx.us,Alert@active911.com";
  }
}
