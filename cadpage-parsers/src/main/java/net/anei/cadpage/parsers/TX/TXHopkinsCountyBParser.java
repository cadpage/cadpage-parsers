package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;

public class TXHopkinsCountyBParser extends DispatchA18Parser {

  public TXHopkinsCountyBParser() {
    super(CITY_LIST, "HOPKINS COUNTY", "TX");
  }

  public String getFilter() {
    return "dispatch@sspd.us";
  }


  private static final String[] CITY_LIST = new String[] {

      // Cities
      "Cumby",
      "SULPHUR SPRINGS",

      // Towns
      "COMO",
      "TIRA",

      // Unincorporated communities
      "ADDRAN",
      "BIRTHRIGHT",
      "BRASHEAR",
      "DIKE",
      "GAFFORD",
      "MILLER GROVE",
      "PICKTON",
      "SALTILLO",
      "SULPHUR BLUFF",

      // Ghost towns
      "DILLON",
      "WHO'D THOUGHT IT"
  };
}
