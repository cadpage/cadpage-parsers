package net.anei.cadpage.parsers.OR;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA22Parser;

/*
 * Klamath County, OR
 */

public class ORKlamathCountyAParser extends DispatchA22Parser {

  public ORKlamathCountyAParser() {
    super(CITY_CODES, "KLAMATH COUNTY", "OR");
    setupGpsLookupTable(ORKlamathCountyParser.GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "kc-911@kc911.us,paging@klamath911.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected String adjustGpsLookupAddress(String addr) {
    return ORKlamathCountyParser.doAdjustGpsLookupAddress(addr);
  }

  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "CV", "CHRISTMAS VALLEY",
      "KF", "KLAMATH FALLS"
  });
}
