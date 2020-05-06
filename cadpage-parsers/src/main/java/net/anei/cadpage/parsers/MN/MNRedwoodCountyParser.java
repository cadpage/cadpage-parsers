package net.anei.cadpage.parsers.MN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;

public class MNRedwoodCountyParser extends DispatchA43Parser {
  
  public MNRedwoodCountyParser() {
    super("REDWOOD COUNTY", "MN");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "16626 KENWOOD AVE",                    "+44.291477,-95.189718"
  });
}
