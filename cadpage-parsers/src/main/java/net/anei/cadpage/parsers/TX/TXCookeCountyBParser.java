package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;

public class TXCookeCountyBParser extends DispatchA18Parser {

  public TXCookeCountyBParser() {
    super(CITY_LIST, "COOKE COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "active911@gvps.org";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CALLISBURG",
      "GAINESVILLE",
      "LINDSAY",
      "MUENSTER",
      "VALLEY VIEW",

      // Towns
      "OAK RIDGE",

      // Census-designated place
      "LAKE KIOWA",

      // Unincorporated communities
      "BULCHER",
      "BURNS CITY",
      "DEXTER",
      "ERA",
      "HOOD",
      "LEO",
      "LOIS",
      "MARYSVILLE",
      "MOSS LAKE",
      "MOUNTAIN SPRINGS",
      "MYRA",
      "ROAD RUNNER",
      "PRAIRIE POINT",
      "ROSSTON",
      "SIVELLS BEND",
      "WALNUT BEND",
      "WOODBINE"
  };

}
