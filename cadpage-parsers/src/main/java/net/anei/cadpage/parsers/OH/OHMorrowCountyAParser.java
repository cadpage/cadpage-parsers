package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

/**
 * Morrow County, OH
 */
public class OHMorrowCountyAParser extends DispatchA1Parser {

  public OHMorrowCountyAParser() {
    super("MORROW COUNTY", "OH");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "mc911@rrohio.com,911text@rrohio.com,911text@mcems.net";
  }

  private static final Pattern RD_NNN_PTN = Pattern.compile("\\bRD +(\\d{2,3})\\b");

  @Override
  public String adjustMapAddress(String sAddress) {
    return RD_NNN_PTN.matcher(sAddress).replaceAll("TOWNSHIP HWY $1");
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "1996 RD 146",                          "+40.435715,-83.010948",
      "77 RD 158",                            "+40.440529,-83.020379",
      "US RT 42 & RD 25",                     "+40.469301,-82.943640"
  });
}
