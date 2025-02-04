package net.anei.cadpage.parsers.TN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA95Parser;


public class TNCoffeeCountyParser extends DispatchA95Parser {

  public TNCoffeeCountyParser() {
    super("COFFEE COUNTY", "TN");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "COFFEECO911@BENLOMAND.NET";
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "21 POWELL RD",                         "+35.407222,-86.173306"
  });
}
