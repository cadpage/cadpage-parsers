package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class KYShelbyCountyBParser extends DispatchSPKParser {

  public KYShelbyCountyBParser() {
    super("SHELBY COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "noreply@public-safety-cloud.com";
  }

}
