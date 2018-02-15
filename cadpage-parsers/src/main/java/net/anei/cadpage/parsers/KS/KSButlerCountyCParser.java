package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSButlerCountyCParser extends DispatchA25Parser {
  
  public KSButlerCountyCParser() {
    super("BUTLER COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "tfollis@cox.net,@augustadps.org";
  }

  
}
