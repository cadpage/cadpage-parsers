package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class OKCanadianCountyAParser extends DispatchBCParser {
  
  public OKCanadianCountyAParser() {
    super("CANADIAN COUNTY", "OK");
  }
  
  @Override
  public String getFilter() {
    return "@cityofelreno.com";
  }
}
