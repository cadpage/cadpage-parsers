package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class TXMaverickCountyParser extends DispatchA27Parser {
  
  public TXMaverickCountyParser() {
    super("MAVERICK COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
