package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA92Parser;

public class TXHarrisCountyESD1CParser extends DispatchA92Parser {
  
  public TXHarrisCountyESD1CParser() {
    super("HARRIS COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "logisids@vlitech.com";
  }

}
