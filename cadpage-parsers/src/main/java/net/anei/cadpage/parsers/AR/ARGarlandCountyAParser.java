package net.anei.cadpage.parsers.AR;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchLifeNetEMSParser;


public class ARGarlandCountyAParser extends DispatchLifeNetEMSParser {

  public ARGarlandCountyAParser() {
    super(CITY_LIST, "GARLAND COUNTY", "AR");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "215 HWY 290",   "+34.412122,-93.089761",
      "1144 HWY 290",  "+34.402439,-93.065960"
  });

  private static final String[] CITY_LIST = new String[]{
    "MOUNTAIN PINE",
    "FOUNTAIN LAKE",
    "JESSEVILLE",  // Misspelled
    "JESSIEVILLE",
    "LONSDALE",
    "HOT SPRINGS",
    "HOT SPRINGS VILLAGE",
    "LAKE HAMILTON",
    "PEARCY",
    "PINEY",
    "ROCKWELL",
    "ROYAL",
    "HALE TWP",
    "HOT SPRINGS TWP",
    "LAKE HAMILTON TWP",
    "WHITTINGTON TWP",

    // Hot springs county
    "BONNERDALE",
    "MALVERN",

    // Montgomery County
    "STORY",

    // Saline County
    "BENTON"
  };
}
