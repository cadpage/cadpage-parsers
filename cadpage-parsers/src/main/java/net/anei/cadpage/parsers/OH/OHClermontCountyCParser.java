package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class OHClermontCountyCParser extends DispatchA1Parser {
  
  public OHClermontCountyCParser() {
    super("CLERMONT COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "utcc@union-township.oh.us";
  }
}
