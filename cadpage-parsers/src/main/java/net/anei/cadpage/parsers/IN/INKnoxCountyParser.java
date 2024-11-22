package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INKnoxCountyParser extends DispatchSPKParser {
  public INKnoxCountyParser() {
    super("KNOX COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "KnoxCoCAD@in.gov,KnoxCoCAD@ipsc.in.gov,KnoxCAD@knoxcounty.in.gov";
  }
}
