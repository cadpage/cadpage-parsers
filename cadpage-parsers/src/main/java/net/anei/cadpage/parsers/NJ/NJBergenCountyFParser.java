package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class NJBergenCountyFParser extends DispatchA27Parser {
  
  public NJBergenCountyFParser() {
    super("BERGEN COUNTY", "NJ", "[A-Z]+\\d+[A-Z]");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
