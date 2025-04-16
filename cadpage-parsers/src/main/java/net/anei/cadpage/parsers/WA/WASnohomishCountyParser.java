package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Snohomish County, WA
 */
public class WASnohomishCountyParser extends GroupBestParser {

  public WASnohomishCountyParser() {
    super(new WASnohomishCountyAParser(), new WASnohomishCountyBParser(),
          new WASnohomishCountyCParser(), new WASnohomishCountyDParser(),
          new WASnohomishCountyEParser(), new WASnohomishCountyFParser(),
          new WASnohomishCountyGParser(), new WASnohomishCountyHParser());
  }


  static final String[] CITY_LIST = new String[]{

    // Cities
    "ARLINGTON",
    "BOTHELL",
    "BRIER",
    "EDMONDS",
    "EVERETT",
    "GOLD BAR",
    "GRANITE FALLS",
    "LAKE STEVENS",
    "LYNNWOOD",
    "MARYSVILLE",
    "MILL CREEK",
    "MONROE",
    "MOUNTLAKE TERRACE",
    "MUKILTEO",
    "SNOHOMISH",
    "STANWOOD",
    "SULTAN",
    "WOODWAY",

    // Towns
    "DARRINGTON",
    "INDEX",

    // Census-designated places
    "ALDERWOOD MANOR",
    "ARLINGTON HEIGHTS",
    "BOTHELL EAST",
    "BOTHELL WEST",
    "BRYANT",
    "BUNK FOSS",
    "CANYON CREEK",
    "CATHCART",
    "CAVALERO",
    "CHAIN LAKE",
    "CLEARVIEW",
    "EASTMONT",
    "ESPERANCE",
    "FOBES HILL",
    "HAT ISLAND",
    "HIGH BRIDGE",
    "LAKE BOSWORTH",
    "LAKE KETCHUM",
    "LAKE ROESIGER",
    "LAKE STICKNEY",
    "LOCHSLOY",
    "MACHIAS",
    "MALTBY",
    "MARTHA LAKE",
    "MAY CREEK",
    "NORTH MARYSVILLE",
    "NORTH SULTAN",
    "NORTHWEST STANWOOD",
    "OSO",
    "SILVANA",
    "SILVER FIRS",
    "STARTUP",
    "SUNDAY LAKE",
    "SWEDE HEAVEN",
    "THREE LAKES",
    "VERLOT",
    "WARM BEACH",
    "WOODS CREEK",

    "KING COUNTY"
  };

}
