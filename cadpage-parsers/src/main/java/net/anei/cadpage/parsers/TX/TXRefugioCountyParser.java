package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXRefugioCountyParser extends DispatchA72Parser {

  public TXRefugioCountyParser() {
    super("REFUGIO COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "texasrefugio@gmail.com";
  }
}
