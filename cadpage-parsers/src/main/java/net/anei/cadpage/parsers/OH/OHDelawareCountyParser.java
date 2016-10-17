package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;



public class OHDelawareCountyParser extends DispatchA1Parser {
  
  public OHDelawareCountyParser() {
    super("DELAWARE COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "del-911@co.delaware.oh.us";
  }
}
