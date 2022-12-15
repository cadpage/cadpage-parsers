package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INBrownCountyParser extends DispatchSPKParser {
  public INBrownCountyParser() {
    super("BROWN COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "htvfd911@aol.com,tonka690@msn.com,BrownCountyCAD@ipsc.in.gov,noreply@public-safety-cloud.com";
  }
}
