package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOPerryCountyParser extends DispatchA33Parser {
  
  
  public MOPerryCountyParser() {
    super("PERRY COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "louisianapd.dispatch@gmail.com";
  }
}