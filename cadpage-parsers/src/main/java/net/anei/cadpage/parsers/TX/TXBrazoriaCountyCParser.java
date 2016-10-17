package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA59Parser;

public class TXBrazoriaCountyCParser extends DispatchA59Parser {
  
  public TXBrazoriaCountyCParser() {
    super("BRAZORIA COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@oystercreekpd.com";
  }
}
