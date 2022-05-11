package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class OHMontgomeryCountyDParser extends DispatchA1Parser {
  
  public OHMontgomeryCountyDParser() {
    super("MONTGOMERY COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "dispatchalerts@oakwood.oh.us";
  }
}
