package net.anei.cadpage.parsers.IA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA38Parser;

public class IAJohnsonCountyParser extends DispatchA38Parser {


  public IAJohnsonCountyParser() {
    super("JOHNSON COUNTY", "IA");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "noreply@jecc-ema.org";
  }

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE =  buildCodeTable(new String[] {
      "750 E FOSTER RD",                      "+41.684935,-91.527715",
      "2640 N SCOTT BLVD",                    "+41.677864,-91.503297"
  });
}
