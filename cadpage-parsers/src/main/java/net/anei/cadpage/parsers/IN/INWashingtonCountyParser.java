package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INWashingtonCountyParser extends DispatchSPKParser {

  public INWashingtonCountyParser() {
    super("WASHINGTON COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "WashingtonCo@ipsc.in.gov,noreply@public-safety-cloud.com";
  }
}
