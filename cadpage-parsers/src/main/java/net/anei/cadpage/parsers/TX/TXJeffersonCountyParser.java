package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Port Aransas, TX
 */

public class TXJeffersonCountyParser extends DispatchA27Parser {

  public TXJeffersonCountyParser() {
    super("JEFFERSON COUNTY", "TX", "\\d{8}");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
