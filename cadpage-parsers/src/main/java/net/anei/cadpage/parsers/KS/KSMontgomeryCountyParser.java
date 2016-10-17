package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSMontgomeryCountyParser extends DispatchA25Parser {
  
  public KSMontgomeryCountyParser() {
    super("MONTGOMERY COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "EnterpolAlerts@coffeyvillepd.org,cad@indypd.com";
  }
}
