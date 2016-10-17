package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * San Marcos County, TX
 */

public class TXHaysCountyCParser extends DispatchA27Parser {
  
  public TXHaysCountyCParser() {
    super("SAN MARCOS COUNTY", "TX", "\\d{8}|[A-Z]+\\d+");
  }
  
  @Override
  public String getFilter() {
    return "noreply@sanmarcostx.gov";
  }
}
