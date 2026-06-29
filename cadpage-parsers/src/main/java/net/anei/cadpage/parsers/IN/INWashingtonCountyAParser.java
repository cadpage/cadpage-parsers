package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INWashingtonCountyAParser extends DispatchSPKParser {

  public INWashingtonCountyAParser() {
    super("WASHINGTON COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "WashingtonCo@ipsc.in.gov,noreply@public-safety-cloud.com";
  }
}
