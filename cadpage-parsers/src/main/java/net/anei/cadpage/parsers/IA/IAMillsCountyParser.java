package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;

public class IAMillsCountyParser extends DispatchA47Parser {

  public IAMillsCountyParser() {
    super("From Mills Co 911", CITY_LIST, "MILLS COUNTY", "IA", null);
  }

  @Override
  public String getFilter() {
    return "911@paging.millscountyiowa.gov";
  }

  private static final String[] CITY_LIST = new String[] {
      // Cities
      "EMERSON",
      "GLENWOOD",
      "HASTINGS",
      "HENDERSON",
      "MALVERN",
      "PACIFIC JUNCTION",
      "SILVER CITY",
      "TABOR",

      // Unincorporated communities
      "RUSHVILLE",

      // Census-designated place
      "MINEOLA",

      // Townships
      "ANDERSON",
      "CENTER",
      "DEER CREEK",
      "GLENWOOD",
      "INDIAN CREEK",
      "INGRAHAM",
      "LYONS",
      "OAK",
      "PLATTVILLE",
      "RAWLES",
      "ST MARYS",
      "SILVER CREEK",
      "WHITE CLOUD"
  };
}
