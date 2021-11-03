package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.GroupBestParser;


public class CTTollandCountyParser extends GroupBestParser {

  public CTTollandCountyParser() {
    super(new CTTollandCountyAParser(), new CTTollandCountyBParser(),
          new CTTollandCountyCParser(), new CTTollandCountyDParser());
  }

  static final String[] CITY_LIST = new String[]{
    "ANDOVER",
    "BOLTON",
    "COLUMBIA",
    "COVENTRY",
    "ELLINGTON",
    "HEBRON",
    "MANSFIELD",
    "SOMERS",
    "STAFFORD",
    "TOLLAND",
    "UNION",
    "VERNON",
    "WILLINGTON",

    "COVENTRY LAKE",
    "SOUTH COVENTRY",
    "CRYSTAL LAKE",
    "STAFFORD SPRINGS",
    "STORRS",
    "CENTRAL SOMERS",
    "ROCKVILLE",
    "MASHAPAUG",

    "WAREHOUSE POINT",

    "UCONN",

    // Fairfield County
    "NEWTOWN",

    // Hartford County
    "BROAD BROOK",
    "EAST GRANDBY",
    "EAST WINDSOR",
    "ENFIELD",
    "GLASTONBURY",
    "HARTFORD",
    "MANCHESTER",
    "MARLBOROUGH",
    "SOUTH WINDSOR",
    "WINDSOR LOCKS",
    "WINDSOR LOCKS EAST",

    // New London County
    "BALTIC",
    "FRANKLIN",
    "LEBANON",

    // Windham county
    "ASHFORD",
    "CANTERBURY",
    "CHAPLIN",
    "BROOKLYN",
    "EASTFORD",
    "HAMPTON",
    "NORTH WINDHAM",
    "SCOTLAND",
    "SOUTH WINDHAM",
    "WILLIMANTIC",
    "WINDHAM",
    "WOODSTOCK"
  };
}

