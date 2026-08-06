package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA92Parser;

public class TXKaufmanCountyCParser extends DispatchA92Parser {

  public TXKaufmanCountyCParser() {
    super("KAUFMAN COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "logisids@vlitech.com";
  }
}
