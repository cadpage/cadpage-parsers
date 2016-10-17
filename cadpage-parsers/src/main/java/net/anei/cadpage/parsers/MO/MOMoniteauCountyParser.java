package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class MOMoniteauCountyParser extends DispatchA25Parser {
  
  public MOMoniteauCountyParser() {
    super("MONITEAU COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "cad@moniteau911.com";
  }
}
