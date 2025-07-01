package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXMorrisCountyParser extends DispatchA72Parser {

  public TXMorrisCountyParser() {
    super("MORRIS COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "morriscountypaging@gmail.com";
  }

}
