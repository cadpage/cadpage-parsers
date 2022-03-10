package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class KYGarrardCountyParser extends DispatchSPKParser {

  public KYGarrardCountyParser() {
    super("GARRARD COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "noreply@interact911.com,noreply@public-safety-cloud.com,bluegrasskyreports@gmail.com";
  }

}
