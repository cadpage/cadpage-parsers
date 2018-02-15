package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class TXDallasCountyEParser extends DispatchA27Parser {
  
  public TXDallasCountyEParser() {
    super("DALLAS COUNTY", "TX", "\\d{8}");
  }
  
  @Override
  public String getFilter() {
    return "rfd@cor.gov";
  }
}
