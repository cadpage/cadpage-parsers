package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOClayCountyCParser extends DispatchA33Parser {
  
  
  public MOClayCountyCParser() {
    super("CLAY COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "ITI@NKC.ORG";
  }
}