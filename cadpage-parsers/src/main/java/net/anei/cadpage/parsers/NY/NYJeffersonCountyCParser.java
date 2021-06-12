package net.anei.cadpage.parsers.NY;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NYJeffersonCountyCParser extends DispatchA19Parser {

  public NYJeffersonCountyCParser() {
    super(CITY_CODES, "JEFFERSON COUNTY", "NY");
  }

  @Override
  public String getFilter() {
    return "fire@co.jefferson.ny.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "CWT", "WATERTOWN",
      "FTD", "FORT DRUM",
      "TLE", "LE REY",
      "TRO", "RODMAN",
      "TWT", "WATERTOWN"
  });

}
