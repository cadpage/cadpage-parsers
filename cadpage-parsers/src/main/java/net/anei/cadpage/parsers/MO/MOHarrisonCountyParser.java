package net.anei.cadpage.parsers.MO;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;


public class MOHarrisonCountyParser extends DispatchBCParser {
  public MOHarrisonCountyParser() {
    super("HARRISON COUNTY", "MO");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "HARCO911@GRM.NET,HARRISON911@PUBLICSAFETYSOFTWARE.NET,HCSO@OMNIGO.COM";
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "24475 E 225TH AVE",                    "+40.360419,-93.996812",
      "1011 MAIN ST",                         "+40.383097,-93.936801"
  });
}
