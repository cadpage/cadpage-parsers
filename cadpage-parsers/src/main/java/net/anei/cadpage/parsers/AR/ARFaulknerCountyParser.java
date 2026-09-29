package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.dispatch.DispatchC12Parser;

public class ARFaulknerCountyParser extends DispatchC12Parser {

  public ARFaulknerCountyParser() {
    super(AUX_CITY_LIST, "FAULKNER COUNTY", "AR");
    removeWords("APARTMENT");
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final String[] AUX_CITY_LIST = new String[] {

      // Cities
      "CONWAY",
      "GREENBRIER",
      "GUY",
      "HOLLAND",
      "MAYFLOWER",
      "QUITMAN",
      "VILONIA",

      // Towns
      "DAMASCUS",
      "ENDERS",
      "ENOLA",
      "MOUNT VERNON",
      "TWIN GROVES",
      "WOOSTER",

      "PULASKI COUNTY"
  };
}
