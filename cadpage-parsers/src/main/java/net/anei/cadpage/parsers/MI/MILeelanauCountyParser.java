package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchA17Parser;


public class MILeelanauCountyParser extends DispatchA17Parser {
  
  public MILeelanauCountyParser() {
    super("LEELANAU COUNTY", "MI");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@co.leelanau.mi.us";
  }
}
