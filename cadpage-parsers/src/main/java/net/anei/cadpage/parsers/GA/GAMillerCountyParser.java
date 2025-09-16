package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA83Parser;

public class GAMillerCountyParser extends DispatchA83Parser {

  public GAMillerCountyParser() {
    super(CITY_LIST, "MILLER COUNTY", "GA", A83_REQ_SENDER);
    setupSpecialStreets("MLK JR");
    addInvalidWords("ENTRY", "YEAR", "YEARS");
  }

  @Override
  public String getFilter() {
    return "millerbaker.ga@ez911map.net,millerbaker.ga@ez911map.net";
  }

  private static final String[] CITY_LIST = new String[] {
      // City
      "COLQUITT",

      // Census-designated place
      "BOYKIN",

      // Unincorporated communities
      "BABCOCK",

      // Baker County
      "NEWTON"
  };
}
