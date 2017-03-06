package net.anei.cadpage.parsers.MT;

import net.anei.cadpage.parsers.dispatch.DispatchA68Parser;

public class MTCascadeCountyAParser extends DispatchA68Parser {
  
  public MTCascadeCountyAParser() {
    super("CASCADE COUNTY", "MT");
  }
  
  @Override
  public String getFilter() {
    return "911text@911intn.org,@greatfallsmt.net";
  }
}
