package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXLibertyCountyCParser extends DispatchA72Parser {
  
  public TXLibertyCountyCParser() {
    super("LIBERTY COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "ips@daytontx.org";
  }
}
