package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXBastropCountyParser extends DispatchA72Parser {

  public TXBastropCountyParser() {
    super("BASTROP COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "bastropactive911@co.bastrop.tx.us";
  }

}
