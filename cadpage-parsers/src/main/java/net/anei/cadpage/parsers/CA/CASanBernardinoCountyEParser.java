package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

public class CASanBernardinoCountyEParser extends DispatchA49Parser {

  public CASanBernardinoCountyEParser() {
    super("SAN BERNARDINO COUNTY", "CA");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "cadalert@adsisoftware.com";
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "307 9TH ST",                           "+35.270310,-116.676936",
      "111 B AV",                             "+35.265800,-116.268700",
      "273 C AV",                             "+35.262100,-116.687700",
      "990 INNER LOOP RD",                    "+35.251500,-116.687600"
  });

}
