package net.anei.cadpage.parsers.MN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;

public class MNFillmoreCountyParser extends DispatchA43Parser {

  public MNFillmoreCountyParser() {
    super("FILLMORE COUNTY", "MN");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "1864 FIRST MINNESOTA",                 "+43.681830,-92.094260"
  });
}
