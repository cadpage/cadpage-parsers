package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class VAWytheCountyBParser extends DispatchA19Parser {

  public VAWytheCountyBParser() {
    super(CITY_CODES, "WYTHE COUNTY", "VA");
  }

  @Override
  public String getFilter() {
    return "RIPNRUN@WYTHECO.ORG";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "RUR", "RURAL RETREAT",
      "WYT", "WYTHEVILLE"
  });

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
