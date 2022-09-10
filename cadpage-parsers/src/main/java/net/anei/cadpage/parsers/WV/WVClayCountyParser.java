package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class WVClayCountyParser extends DispatchSPKParser {
  
  public WVClayCountyParser() {
    super("CLAY COUNTY", "WV");
  }
  
  @Override
  public String getFilter() {
    return "text.clay911@gmail.com,ClayCoCAD@911.com";
  }

}
