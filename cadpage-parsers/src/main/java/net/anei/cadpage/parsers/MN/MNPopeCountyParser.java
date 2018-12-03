package net.anei.cadpage.parsers.MN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;

public class MNPopeCountyParser extends DispatchA43Parser {
  public MNPopeCountyParser() {
    super("POPE COUNTY", "MN");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "11550 200TH ST",           "45.607180,-95.171412"
  });
}
