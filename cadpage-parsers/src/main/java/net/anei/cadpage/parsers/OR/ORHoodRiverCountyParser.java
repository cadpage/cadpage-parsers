package net.anei.cadpage.parsers.OR;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA22Parser;

/*
Hood River County, OR
 */

public class ORHoodRiverCountyParser extends DispatchA22Parser {

  public ORHoodRiverCountyParser() {
    super(CITY_CODES, "HOOD RIVER COUNTY", "OR");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "hr911paging@co.hood-river.or.us,hr911paging@mail.hoodrivercounty.gov";
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "MP 41 I84",                            "+45.636029,-121.945340",
      "MP 42 I84",                            "+45.645378,-121.922170",
      "MP 43 I84",                            "+45.652773,-121.905224",
      "MP 44 I84",                            "+45.658636,-121.898795",
      "MP 45 I84",                            "+45.672525,-121.876681",
      "MP 46 I84",                            "+45.676456,-121.859427",
      "MP 47 I84",                            "+45.685888,-121.841539",
      "MP 48 I84",                            "+45.696548,-121.828107",
      "MP 49 I84",                            "+45.696982,-121.808211",
      "MP 50 I84",                            "+45.692928,-121.787296",
      "MP 51 I84",                            "+45.690950,-121.773732",
      "MP 52 I84",                            "+45.691195,-121.750529",
      "MP 53 I84",                            "+45.690516,-121.726593",
      "MP 54 I84",                            "+45.687832,-121.700545",
      "MP 55 I84",                            "+45.690467,-121.688482",
      "MP 56 I84",                            "+45.696548,-121.669209",
      "MP 57 I84",                            "+45.698994,-121.650339",
      "MP 58 I84",                            "+45.699959,-121.629194",
      "MP 59 I84",                            "+45.705355,-121.587936",
      "MP 60 I84",                            "+45.704927,-121.592085",
      "MP 61 I84",                            "+45.707368,-121.569263",
      "MP 62 I84",                            "+45.711689,-121.548947",
      "MP 63 I84",                            "+45.712910,-121.531834",
      "MP 64 I84",                            "+45.711280,-121.513294",
      "MP 65 I84",                            "+45.709986,-121.493674",
      "MP 66 I84",                            "+45.701744,-121.473780",
      "MP 67 I84",                            "+45.696133,-121.456608",
      "MP 68 I84",                            "+45.691584,-121.440381",
      "MP 69 I84",                            "+45.686146,-121.420575"
  });

  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "CL", "CASCADE LOCKS",
      "HR", "HOOD RIVER",
      "PD", "PARKDALE"
  });
}
