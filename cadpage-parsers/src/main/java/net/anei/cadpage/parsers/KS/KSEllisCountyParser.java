package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSEllisCountyParser extends DispatchA25Parser {
  
  public KSEllisCountyParser() {
    super("ELLIS COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "1-HPDDispatch@haysusa.com";
  }
}
