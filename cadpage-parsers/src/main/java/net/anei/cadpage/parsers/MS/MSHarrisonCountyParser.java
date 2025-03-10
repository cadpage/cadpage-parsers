package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.GroupBestParser;


public class MSHarrisonCountyParser extends GroupBestParser {

  @Override
  public String getLocName() {
    return "Harrison County, MS";
  }

  public MSHarrisonCountyParser() {
    super(new MSHarrisonCountyAParser(),
          new MSHarrisonCountyCParser(), new MSHarrisonCountyDParser(),
          new MSHarrisonCountyEParser(), new MSHarrisonCountyFParser(),
          new MSHarrisonCountyGParser());
  }

  static final String[] CITY_LIST = new String[]{

      // Cities
      "BILOXI",
      "D'IBERVILLE",
      "DIBERVILLE",
      "GULFPORT",
      "LONG BEACH",
      "PASS CHRISTIAN",

      // Census-designated places
      "DELISLE",
      "HENDERSON POINT",
      "LYMAN",
      "SAUCIER",

      // Unincorporated communities
      "CUEVAS",
      "HOWISON",
      "LIZANA"
  };

}
