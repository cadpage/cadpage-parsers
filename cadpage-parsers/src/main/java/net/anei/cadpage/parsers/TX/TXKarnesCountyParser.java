package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXKarnesCountyParser extends DispatchA72Parser {

  public TXKarnesCountyParser() {
    super("KARNES COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "kcemscommand@gmail.com";
  }

}
