package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INNewtonCountyParser extends DispatchSPKParser {

  public INNewtonCountyParser() {
    super("NEWTON COUNTY","IN");
  }

  @Override
  public String getFilter() {
    return "noreply@public-safety-cloud.com";
  }
}
