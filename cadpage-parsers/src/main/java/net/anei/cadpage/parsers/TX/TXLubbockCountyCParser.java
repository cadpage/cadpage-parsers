package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXLubbockCountyCParser extends DispatchA72Parser {
  
  public TXLubbockCountyCParser() {
    super("LUBBOCK COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "bgdickerson1@outlook.com";
  }
}
