package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXGreggCountyCParser extends DispatchA72Parser {

  public TXGreggCountyCParser() {
    super("GREGG COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "donotreply@cityofgladewater.com";
  }
}
