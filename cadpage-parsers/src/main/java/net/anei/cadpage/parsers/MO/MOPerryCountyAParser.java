package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOPerryCountyAParser extends DispatchA33Parser {
  
  
  public MOPerryCountyAParser() {
    super("PERRY COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "louisianapd.dispatch@gmail.com,PERRY@OMNIGO.COM";
  }
}