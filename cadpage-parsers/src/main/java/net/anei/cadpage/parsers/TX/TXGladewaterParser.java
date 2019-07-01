package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class TXGladewaterParser extends DispatchA19Parser {
  
  public TXGladewaterParser() {
    super("GLADEWATER", "TX");
  }
  
  @Override
  public String getFilter() {
    return "noreply@responsemaster.net";
  }

}
