package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class ILCookCountyFParser extends DispatchA27Parser {
  
  public ILCookCountyFParser() {
    super("COOK COUNTY", "IL", "[A-Z]+\\d*");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org,noreply@everbridge.net";
  }
}
