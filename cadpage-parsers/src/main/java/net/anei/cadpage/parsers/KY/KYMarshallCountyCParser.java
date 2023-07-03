package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYMarshallCountyCParser extends DispatchA27Parser {

  public KYMarshallCountyCParser() {
    super(CITY_LIST, "MARSHALL COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "marshallco911ky@cissystem.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "BENTON",
      "CALVERT CITY",
      "HARDIN",

      // Census-designated place
      "GILBERTSVILLE",

      // Other unincorporated communities
      "AURORA",
      "BIG BEAR AREA",
      "BREWERS",
      "BRIENSBURG",
      "DRAFFENVILLE",
      "FAIRDEALING",
      "HARVEY",
      "MOORS CAMP AREA",
      "OAK LEVEL",
      "OLIVE",
      "PALMA",
      "POSSUM TROT",
      "SHARPE",
      "TATUMSVILLE"
  };

}
