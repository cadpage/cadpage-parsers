package net.anei.cadpage.parsers.NC;


import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

/**
 * Gates County, NC
 */

public class NCGatesCountyParser extends DispatchA71Parser {

  public NCGatesCountyParser() {
    super("GATES COUNTY", "NC");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String adjustMapAddress(String addr) {
    return addr.replace(" TRLR PARK", " TR PARK");
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "19 ROBERT ROBINSON LN",                "+36.458840,-76.673580",
      "22 ROBERT ROBINSON LN",                "+36.458330,-76.673730"
  });
}
