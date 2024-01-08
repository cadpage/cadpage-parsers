package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYDaviessCountyParser extends GroupBestParser {

  public KYDaviessCountyParser() {
    super(new KYDaviessCountyAParser(), new KYDaviessCountyBParser());
  }


  static final String[] CITY_LIST = new String[]{
      "OWENSBORO",

      "WEST DAVIESS",

      "CURDSVILLE",
      "DELAWARE",
      "MOSELEYVILLE",
      "PANTHER",
      "PETTIT",
      "ROME",
      "SAINT JOSEPH",
      "SORGHO",
      "STANLEY",
      "SUTHERLAND",
      "TUCK",
      "UTICA",
      "WEST LOUISVILLE",

      "EAST DAVIESS",

      "DERMONT",
      "ENSOR",
      "HABIT",
      "KNOTTSVILLE",
      "MACEO",
      "MASONVILLE",
      "PHILPOT",
      "THRUSTON",
      "WHITESVILLE",
      "YELVINGTON",

      // Hancock County
      "HANCOCK COUNTY",
      "HANCOCK CO",
      "HAWESVILLE",
      "LEWISPORT",

      // Mclean County
      "MCLEAN COUNTY",
      "MCLEAN CO",
      "CALHOUN",

      // Ohio County
      "OHIO COUNTY",
      "OHIO CO",
      "HARTFORD",
      "REYNOLDS STATION"
  };

}
