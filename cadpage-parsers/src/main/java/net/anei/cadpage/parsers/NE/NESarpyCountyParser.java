package net.anei.cadpage.parsers.NE;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class NESarpyCountyParser extends DispatchH03Parser {
  
  public NESarpyCountyParser() {
    super("SARPY COUNTY", "NE");
  }
  
  @Override
  public String getFilter() {
    return "SCCC@caddouglascounty-ne.gov";
  }
}
