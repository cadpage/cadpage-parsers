package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.dispatch.DispatchA52Parser;

public class FLLakeCountyBParser extends DispatchA52Parser {
  
  public FLLakeCountyBParser() {
    super("LAKE COUNTY", "FL");
  }
  
  @Override
  public String getFilter() {
    return "tap@yourdomain.com";
  }

}
