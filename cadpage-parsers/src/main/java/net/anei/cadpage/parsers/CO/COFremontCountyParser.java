package net.anei.cadpage.parsers.CO;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class COFremontCountyParser extends DispatchA57Parser {


  public COFremontCountyParser() {
    super("FREMONT COUNTY", "CO");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "crcasmtp@hamilton.net";
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "57500 US HWY 50",      "+38.443152,-105.155806"
  });
}
