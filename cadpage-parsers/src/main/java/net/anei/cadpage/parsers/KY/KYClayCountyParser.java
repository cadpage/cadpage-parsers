package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class KYClayCountyParser extends DispatchSPKParser {
  
  public KYClayCountyParser() {
    super("CLAY COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "clay.co.ky911@gmail.com";
  }
}