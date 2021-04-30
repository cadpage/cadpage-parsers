package net.anei.cadpage.parsers.KS;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * Riley County, KS
 */
public class KSRileyCountyParser extends DispatchA19Parser {

  public KSRileyCountyParser() {
    super("RILEY COUNTY", "KS");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "spillmancad@pelhamonline.com,spillmancad@rileycountypolice.com,spillmancad@rileycountypolice.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "100 MANHATTAN TOWN CTR",    "39.178923, -96.558442",
      "100 MANHATTAN TOWN CENTER", "39.178923, -96.558442"
  });
}
