package net.anei.cadpage.parsers.WV;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class WVPendletonCountyParser extends DispatchSPKParser {

  public WVPendletonCountyParser() {
    super("PENDLETON COUNTY", "WV");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "pendleton@shentel.net,@pendletoncountywv.gov,911CAD@pendletoncountywv.gov";
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "822 SHADY HOLLOW LN",                  "+38.682795,-79.468285"
  });
}
