package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INClayCountyParser extends DispatchSPKParser {
  public INClayCountyParser() {
    super("CLAY COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "ClayCoCAD@ipsc.in.gov,noreply@public-safety-cloud.com";
  }
}
