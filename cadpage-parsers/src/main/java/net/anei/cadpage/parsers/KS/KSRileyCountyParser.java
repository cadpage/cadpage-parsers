package net.anei.cadpage.parsers.KS;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * Riley County, KS
 */
public class KSRileyCountyParser extends DispatchA19Parser {

  public KSRileyCountyParser() {
    super(CITY_CODES, "RILEY COUNTY", "KS");
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

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "MN2", "MANHATTAN",
      "MN3", "MANHATTAN",
      "MN6", "MANHATTAN"
  });

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "100 MANHATTAN TOWN CTR",    "39.178923, -96.558442",
      "100 MANHATTAN TOWN CENTER", "39.178923, -96.558442"
  });
}
