package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA92Parser;

public class TXHidalgoCountyCParser extends DispatchA92Parser {
  
  public TXHidalgoCountyCParser() {
    super("HIDALGO COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "logisids@vlitech.com";
  }
}
