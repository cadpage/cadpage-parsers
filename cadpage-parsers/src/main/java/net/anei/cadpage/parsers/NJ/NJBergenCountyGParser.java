package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NJBergenCountyGParser extends DispatchA19Parser {
  
  public NJBergenCountyGParser() {
    super("BERGEN COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "flvac@fairlawnpd.com";
  }
}
