package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA77Parser;

public class OHSanduskyCountyParser extends DispatchA77Parser {

  public OHSanduskyCountyParser() {
    super("Fire Call", "SANDUSKY COUNTY", "OH");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "888 N CR 260",                         "+41.366375,-82.954555"
  });

}
