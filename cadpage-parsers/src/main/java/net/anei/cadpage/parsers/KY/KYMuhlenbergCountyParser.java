package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYMuhlenbergCountyParser extends GroupBestParser {

  public KYMuhlenbergCountyParser() {
    super(new KYMuhlenbergCountyAParser(), new KYMuhlenbergCountyBParser());
  }

  static final String[] CITY_LIST = new String[]{
    "BEECH CREEK",
    "BEECHMONT",
    "BELTON",
    "BREMEN",
    "BROWDER",
    "CENTRAL CITY",
    "DEPOY",
    "DRAKESBORO",
    "DUNMOR",
    "ENNIS",
    "GRAHAM",
    "GREENVILLE",
    "POWDERLY",
    "SOUTH CARROLLTON",

    // Logan County
    "LEWISBURG"
  };
}
