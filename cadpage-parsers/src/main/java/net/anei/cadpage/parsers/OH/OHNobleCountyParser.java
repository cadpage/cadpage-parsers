package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class OHNobleCountyParser extends DispatchA1Parser {
  
  public OHNobleCountyParser() {
    super("NOBLE COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "@noblecountysheriffsoffice.com";
  }
}
