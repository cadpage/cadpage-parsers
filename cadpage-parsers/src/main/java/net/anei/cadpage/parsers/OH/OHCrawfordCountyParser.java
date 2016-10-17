package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class OHCrawfordCountyParser extends DispatchA1Parser {
  
  public OHCrawfordCountyParser() {
    super("CRAWFORD COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "crawfordcountysheriffohio.com";
  }
}
