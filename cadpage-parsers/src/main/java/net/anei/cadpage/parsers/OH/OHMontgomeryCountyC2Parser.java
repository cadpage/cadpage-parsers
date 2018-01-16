package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class OHMontgomeryCountyC2Parser extends DispatchH03Parser {
  
  public OHMontgomeryCountyC2Parser() {
    super("MONTGOMERY COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "@mcohiosheriff.org";
  }
}