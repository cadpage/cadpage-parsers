package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class OKCanadianCountyParser extends DispatchBCParser {
  
  public OKCanadianCountyParser() {
    super("CANADIAN COUNTY", "OK");
  }
  
  @Override
  public String getFilter() {
    return "@cityofelreno.com";
  }
}
