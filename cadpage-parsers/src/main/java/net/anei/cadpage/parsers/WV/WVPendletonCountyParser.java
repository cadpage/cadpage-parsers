package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class WVPendletonCountyParser extends DispatchSPKParser {

  public WVPendletonCountyParser() {
    super("PENDLETON COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "pendleton@shentel.net,@pendletoncountywv.gov";
  }
}
