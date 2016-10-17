package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOAudrainCountyParser extends DispatchA33Parser {
  
  
  public MOAudrainCountyParser() {
    super("AUDRAIN COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "CAD@AUDRAIN911.ORG";
  }
}