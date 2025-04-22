package net.anei.cadpage.parsers.FL;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;


public class FLOkaloosaCountyAParser extends DispatchProphoenixParser {

  public FLOkaloosaCountyAParser() {
    super(CITY_CODES, CITY_LIST, "OKALOOSA COUNTY", "FL");
    setupSpecialStreets("CALLE DE TALENCIA");
  }

  @Override
  public String getFilter() {
    return "serviceaccount@sheriff-okaloosa.org";
  }

  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = addr.replace("SCENIC HIGHWAY 98", "HWY 98");
    return super.adjustMapAddress(addr);
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "CRESTVIEW",
      "DESTIN",
      "FORT WALTON BEACH",
      "LAUREL HILL",
      "MARY ESTHER",
      "NICEVILLE",
      "VALPARAISO",

      // Towns
      "CINCO BAYOU",
      "SHALIMAR",

      // Census-designated places
      "EGLIN AFB",
      "LAKE LORRAINE",
      "OCEAN CITY",
      "WRIGHT",

      // Other unincorporated communities
      "BAKER",
      "BLACKMAN",
      "BLUEWATER BAY",
      "CAMPTON",
      "DEERLAND",
      "DORCAS",
      "ESCAMBIA FARMS",
      "FLOROSA",
      "GARDEN CITY",
      "HOLT",
      "MILLIGAN",
      "OKALOOSA ISLAND",
      "SEMINOLE",
      "SVEA",
      "TIMPOOCHEE",
      "VILLA TASSO",
      "WYNNEHAVEN BEACH"
  };

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "FT WALTON BCH",    "FORT WALTON BEACH"
  });
}
