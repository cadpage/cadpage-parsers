package net.anei.cadpage.parsers.KY;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class KYPendletonCountyBParser extends DispatchSPKParser {
  public KYPendletonCountyBParser() {
    super("PENDLETON COUNTY", "KY");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "noreply@interact911.com,noreply@public-safety-cloud.com,Ksp.NGCAD@ky.gov";
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "20 HI & DRY LN",                       "+38.631508,-84.315271"

  });
}
