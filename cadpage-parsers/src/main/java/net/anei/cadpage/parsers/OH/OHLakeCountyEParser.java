package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchC11Parser;

public class OHLakeCountyEParser extends DispatchC11Parser {

  public OHLakeCountyEParser() {
    super(CITY_CODES, "LAKE COUNTY", "OH", C11_INFO);
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CO", "CONCORD TWP",
      "FH", "FAIRPORT HARBOR",
      "GR", "GRAND RIVER",
      "KH", "KIRTLAND HILLS",
      "LE", "LEROY TWP",
      "MA", "MADISON",
      "MV", "MADISON",
      "NP", "NORTH PERRY",
      "PA", "PAINSVILLE",
      "PE", "PERRY",
      "PC", "PAINSVILLE",
      "PV", "PERRY",
      "WA", "WAITE HILL",
      "WH", "WILLOUGHBY HILLS"
  });
}
