package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA44Parser;

public class OHCuyahogaCountyBParser extends DispatchA44Parser {

  public OHCuyahogaCountyBParser() {
    super("CUYAHOGA COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "alert911@parmajustice.net";
  }
}
