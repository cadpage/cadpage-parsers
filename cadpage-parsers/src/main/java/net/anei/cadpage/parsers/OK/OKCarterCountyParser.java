package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.GroupBestParser;

/**
Carter County, OK

 */
public class OKCarterCountyParser extends GroupBestParser {
  public OKCarterCountyParser() {
    super(new OKCarterCountyAParser(), new OKCarterCountyBParser(), new OKCarterCountyCParser());
  }

  static final String[] CITY_LIST = new String[]{
    "CARTER COUNTY",
    "ARDMORE",
    "DICKSON",
    "DRIPPING SPRINGS",
    "GENE AUTRY",
    "HEALDTON",
    "LONE GROVE",
    "RATLIFF CITY",
    "SPRINGER",
    "TATUMS",
    "WILSON",

    "JEFFERSON COUNTY",
    "RINGLING"
  };

}
