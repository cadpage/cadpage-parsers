package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOAndrewCountyBParser extends DispatchBCParser {
  
  public MOAndrewCountyBParser() {
    super("ANDREW COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@ANDREWCOUNTY.COM,NOREPLY@OMNIGO.COM";
  }
}
