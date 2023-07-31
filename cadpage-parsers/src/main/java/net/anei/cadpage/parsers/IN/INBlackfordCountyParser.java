package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA38Parser;

public class INBlackfordCountyParser extends DispatchA38Parser {

  public INBlackfordCountyParser() {
    super("BLACKFORD COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "bcsdispatch@Blackfordcounty.in.gov";
  }
}
