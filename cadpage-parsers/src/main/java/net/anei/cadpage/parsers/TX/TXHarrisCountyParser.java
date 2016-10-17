package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Harris County, TX
 */

public class TXHarrisCountyParser extends DispatchA27Parser {
  
  public TXHarrisCountyParser() {
    super("HARRIS COUNTY", "TX", "\\d{8}");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
