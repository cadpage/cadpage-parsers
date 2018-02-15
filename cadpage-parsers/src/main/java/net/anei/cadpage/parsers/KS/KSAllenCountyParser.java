package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSAllenCountyParser extends DispatchA25Parser {
  
  public KSAllenCountyParser() {
    super("ALLEN COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "reports@allencounty911.org";
  }

  
}
