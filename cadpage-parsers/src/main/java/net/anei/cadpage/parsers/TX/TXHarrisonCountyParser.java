package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXHarrisonCountyParser extends DispatchA72Parser {

  public TXHarrisonCountyParser() {
    super("HARRISON COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "intern@co.harrison.tx.us";
  }
}
