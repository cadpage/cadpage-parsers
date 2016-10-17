package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;


/**
 * Delaware County, IN
 */
public class INDelawareCountyParser extends DispatchA9Parser {
  
  public INDelawareCountyParser() {
    super(null, "DELAWARE COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "admin@co.delaware.in.us,@edispatches.com";
  }
  
}
