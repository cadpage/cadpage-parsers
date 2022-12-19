package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;


public class OHMontgomeryCountyAParser extends DispatchH03Parser {

  public OHMontgomeryCountyAParser() {
    super("MONTGOMERY COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "@mcohiosheriff.org";
  }
}
