package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.GroupBestParser;


public class CTFairfieldCountyParser extends GroupBestParser {
  
  public CTFairfieldCountyParser() {
    super(new CTFairfieldCountyAParser(), new CTFairfieldCountyBParser(), new CTFairfieldCountyCParser());
  }
  
  static String[] CITY_LIST = new String[]{
    "EASTON",
    "MONROE",
    "SHELTON",
    "STRATFORD",
    "TRUMBULL",
    "WESTON"
  };
}
