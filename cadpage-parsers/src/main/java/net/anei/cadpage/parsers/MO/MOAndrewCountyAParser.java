package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class MOAndrewCountyAParser extends DispatchA33Parser {
  
  public MOAndrewCountyAParser() {
    super("ANDREW COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@ANDREWCOUNTY.COM,NOREPLY@OMNIGO.COM";
  }
}
