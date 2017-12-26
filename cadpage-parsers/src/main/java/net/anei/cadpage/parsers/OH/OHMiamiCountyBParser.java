package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class OHMiamiCountyBParser extends DispatchA19Parser {
  
  public OHMiamiCountyBParser() {
    super("MIAMI COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "CAD@shelbycountysheriff.com,@alert.active911.com";
  }
}
