package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;



public class WVJeffersonCountyParser extends DispatchA19Parser {
  
  public WVJeffersonCountyParser() {
    super("JEFFERSON COUNTY", "WV");
  }
  
  @Override
  public String getFilter() {
    return "CAD911@jeffersoncountywv.org";
  }
}
