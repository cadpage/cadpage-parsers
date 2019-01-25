package net.anei.cadpage.parsers.MN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;

public class MNLacQuiParleCountyParser extends DispatchA43Parser {
  
  public MNLacQuiParleCountyParser() {
    super("LAC QUI PARLE COUNTY", "MN");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "1372 310TH ST",                        "+45.111060,-96.379520"
  });
}
