package net.anei.cadpage.parsers.MN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Carver County, MN
 */

public class MNCarverCountyParser extends DispatchA27Parser {
  
  public MNCarverCountyParser() {
    super("CARVER COUNTY", "MN", "\\d{8}|[-A-Z0-9]+");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "relay@co.carver.mn.us";
  }
  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "HIGHWAY 212 & HIGHWAY 284",        "+44.765305,-93.781210",
      "10TH ST E & FOX RUN RD",           "+44.840082,-93.786578"
  });
}
