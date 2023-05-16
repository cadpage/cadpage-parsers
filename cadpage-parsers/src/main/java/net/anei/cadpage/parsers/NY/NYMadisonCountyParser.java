package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;


/*
Madison County, NY

*/


public class NYMadisonCountyParser extends GroupBestParser {

  public NYMadisonCountyParser() {
    super(new NYMadisonCountyAParser(),
          new NYMadisonCountyBParser(),
          new NYMadisonCountyCParser(),
          new NYMadisonCountyDParser(),
          new GroupBlockParser(),
          new NYMadisonCountyGLASParser());
  }

  static final String[] CITY_LIST = new String[]{
      "BRIDGEPORT",
      "BROOKFIELD",
      "CANASTOTA",
      "CANASTOTA VIL",
      "CANASTOTA VILLAGE",
      "CAZENOVIA",
      "CAZENOVIA VIL",
      "CAZENOVIA VILLAGE",
      "CHITTENANGO",
      "DERUYTER",
      "EARLVILLE",
      "EARLVILLE VIL",
      "EARLVILLE VILLAGE",
      "EATON",
      "FENNER",
      "GEORGETOWN",
      "HAMILTON",
      "HAMILTON VIL",
      "HAMILTON VILLAGE",
      "LEBANON",
      "LENOX",
      "LINCOLN",
      "MADISON",
      "MADISON VIL",
      "MADISON VILLAGE",
      "MADISON COUNTY",
      "MORRISVILLE",
      "MORRISVILLE VIL",
      "MORRISVILLE VILLAGE",
      "MORRISVILLE VILLAGE-SUNY",
      "MUNNSVILLE",
      "MUNNSVILLE VIL",
      "MUNNSVILLE VILLAGE",
      "NELSON",
      "NELSON VIL",
      "NELSON VILLAGE",
      "ONEIDA",
      "ONEIDA CITY",
      "SMITHFIELD",
      "STOCKBRIDGE",
      "SULLIVAN",
      "WAMPSVILLE",
      "WAMPSVILLE VIL",
      "WAMPSVILLE VILLAGE"
    };

}
