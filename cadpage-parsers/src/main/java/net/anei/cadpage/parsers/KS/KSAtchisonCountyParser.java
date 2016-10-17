package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSAtchisonCountyParser extends DispatchA25Parser {
  
  public KSAtchisonCountyParser() {
    super("ATCHISON COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "EnterpolAlerts@atchisonlec.org";
  }

  
}
