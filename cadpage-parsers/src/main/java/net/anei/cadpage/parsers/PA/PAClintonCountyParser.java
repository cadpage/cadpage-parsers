package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchA5Parser;

/**
 Clinton County, PA
 */
public class PAClintonCountyParser extends DispatchA5Parser {
  
  public PAClintonCountyParser() {
    super("CLINTON COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "@CLINTONCOUNTYPA.COM,@S1014D6A";
  }
}