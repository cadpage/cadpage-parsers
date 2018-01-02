package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class NJBergenCountyFParser extends DispatchA27Parser {
  
  public NJBergenCountyFParser() {
    super("BERGEN COUNTY", "NJ", "[A-Z]{1,6}|[A-Za-z]+\\d+");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org,donotreply@lawsoft-inc.com";
  }
}
