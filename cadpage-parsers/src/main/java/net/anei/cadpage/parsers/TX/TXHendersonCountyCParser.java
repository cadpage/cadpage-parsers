package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXHendersonCountyCParser extends DispatchA72Parser {

  public TXHendersonCountyCParser() {
    super("HENDERSON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "active911@athenstx.gov";
  }

}
