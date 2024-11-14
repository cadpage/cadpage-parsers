package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXBrazoriaCountyCParser extends DispatchA72Parser {

  public TXBrazoriaCountyCParser() {
    super("BRAZORIA COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "rms@cityofbrazoria.org,wcpdactive911@gmail.com";
  }

}
