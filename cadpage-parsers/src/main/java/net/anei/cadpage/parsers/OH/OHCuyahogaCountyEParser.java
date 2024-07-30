package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class OHCuyahogaCountyEParser extends DispatchH03Parser {

  public OHCuyahogaCountyEParser() {
    super("CUYAHOGA COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "@cvdispatch.net";
  }
}
