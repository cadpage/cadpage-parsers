package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXBexarCountyCParser extends DispatchA72Parser {
  
  public TXBexarCountyCParser() {
    super("BEXAR COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "cadrms@cityofkirby.org";
  }
}
