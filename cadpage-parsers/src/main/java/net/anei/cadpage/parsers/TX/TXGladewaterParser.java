package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA53Parser;

public class TXGladewaterParser extends DispatchA53Parser {
  
  public TXGladewaterParser() {
    super("GLADEWATER", "TX");
  }
  
  @Override
  public String getFilter() {
    return "@cityofgladewater.com";
  }

}
