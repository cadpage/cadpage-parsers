package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class NJBergenCountyHParser extends DispatchA27Parser {
  
  public NJBergenCountyHParser() {
    super("BERGEN COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "@bcpsoc.com";
  }
}
