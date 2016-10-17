package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class KYPendletonCountyBParser extends DispatchSPKParser {
  public KYPendletonCountyBParser() {
    super("PENDLETON COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "noreply@interact911.com";
  }
}
