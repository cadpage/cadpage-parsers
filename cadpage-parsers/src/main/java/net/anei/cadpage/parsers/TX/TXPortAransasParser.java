package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Port Aransas, TX
 */

public class TXPortAransasParser extends DispatchA27Parser {
  
  public TXPortAransasParser() {
    super("PORT ARANSAS", "TX", "\\d{8}|PAEMS");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cis.com,noreply@cisusa.org";
  }
}
