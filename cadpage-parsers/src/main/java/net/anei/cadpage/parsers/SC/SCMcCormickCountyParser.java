package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;


public class SCMcCormickCountyParser extends DispatchSPKParser {
  
  public SCMcCormickCountyParser() {
    super("MCCORMICK COUNTY", "SC");
  }

  @Override
  public String getFilter() {
    return "cadalerts@mccormickcountysc.org";
  }
}
