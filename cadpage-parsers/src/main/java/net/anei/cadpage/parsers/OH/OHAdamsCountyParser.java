package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class OHAdamsCountyParser extends DispatchA1Parser {
  
  public OHAdamsCountyParser() {
    super("ADAMS COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@adamscountyoh.gov";
  }
}
