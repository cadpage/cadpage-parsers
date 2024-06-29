package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCSurryCountyBParser extends DispatchSouthernParser {

  public NCSurryCountyBParser() {
    super(CITY_LIST, "SURRY COUNTY", "NC", 
          DSFLG_ADDR | DSFLG_BAD_PLACE | DSFLG_NAME | DSFLG_PHONE | DSFLG_ID | DSFLG_TIME | DSFLG_PROC_EMPTY_FLDS);
  }

  private static final String[] CITY_LIST = new String[] {
    // City
    "MOUNT AIRY",

    // Towns
    "DOBSON",
    "ELKIN",
    "PILOT MOUNTAIN",

    // Census-designated places
    "FLAT ROCK",
    "LOWGAP",
    "TOAST",
    "WHITE PLAINS",
    "TOWNSHIPS",
    "BRYAN",
    "DOBSON",
    "ELDORA",
    "ELKIN",
    "FRANKLIN",
    "LONG HILL",
    "MARSH",
    "MOUNT AIRY",
    "PILOT",
    "ROCKFORD",
    "SHOALS",
    "SILOAM",
    "SOUTH WESTFIELD",
    "STEWARTS CREEK",
    "WESTFIELD",

    // Unincorporated communities
    "ALBION",
    "ARARAT",
    "ASH HILL",
    "BANNERTOWN",
    "BLACKWATER",
    "BLEVINS STORE",
    "BOONES HILL",
    "BOTTOM",
    "BURCH",
    "CEDAR HILL",
    "COMBSTOWN",
    "COPELAND",
    "CROOKED OAK",
    "CRUTCHFIELD",
    "DEVOTION",
    "FAIRVIEW",
    "FRANKLIN",
    "HILLS GROVE",
    "HOLLY SPRINGS",
    "INDIAN GROVE",
    "JENKINSTOWN",
    "LADONIA",
    "LEVEL CROSS",
    "LITTLE RICHMOND",
    "LONG HILL",
    "MOUNT HERMAN",
    "MOUNTAIN PARK",
    "MULBERRY",
    "NEW HOPE",
    "OAK GROVE",
    "PINE HILL",
    "PINE RIDGE",
    "POPLAR SPRINGS",
    "RED BRUSH",
    "ROCKFORD",
    "ROUND PEAK",
    "SALEM",
    "SALEM FORK",
    "SHELTONTOWN",
    "SHOALS",
    "SILOAM",
    "SLATE MOUNTAIN",
    "STATE ROAD",
    "STONY KNOLL",
    "TURKEY FORD",
    "UNION CROSS",
    "UNION HILL",
    "WESTFIELD",
    "WHITE SULPHUR SPRINGS",
    "WOODVILLE",
    "ZEPHYR",
  };
}
