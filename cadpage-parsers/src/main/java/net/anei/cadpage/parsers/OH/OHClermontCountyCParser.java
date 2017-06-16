package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA52Parser;

public class OHClermontCountyCParser extends DispatchA52Parser {
  
  public OHClermontCountyCParser() {
    super("CLERMONT COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "PageGate@clermontcountyohio.gov,1027194726673";
  }
}
