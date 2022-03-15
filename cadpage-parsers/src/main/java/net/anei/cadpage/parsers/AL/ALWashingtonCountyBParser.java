package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ALWashingtonCountyBParser extends DispatchSouthernParser {

  public ALWashingtonCountyBParser() {
    super(CITY_LIST, "WASHINGTON COUNTY", "AL",
          DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_X  | DSFLG_NAME | DSFLG_PHONE | DSFLG_CODE | DSFLG_ID | DSFLG_TIME);
  }

  @Override
  public String getFilter() {
    return "Dispatch@wcal911.com";
  }

  private static final String[] CITY_LIST = new String[] {

      // Towns
      "CHATOM",
      "MCINTOSH",
      "MILLRY",

      // Census-designated places
      "CALVERT",
      "CULLOMBURG",
      "DEER PARK",
      "FAIRFORD",
      "FRUITDALE",
      "HOBSON",
      "LEROY",
      "MALCOLM",
      "ST STEPHENS",
      "SIMS CHAPEL",
      "TIBBIE",
      "VINEGAR BEND",

      // Unincorporated communities
      "CORTELYOU",
      "ESCATAWPA",
      "FRANKVILLE",
      "LATON HILL",
      "SUNFLOWER",
      "WAGARVILLE",
      "YELLOW PINE",
      "YARBO",

      // Ghost town
      "WAKEFIELD",

      // Mobile County
      "CITRONELLE"
  };

}
