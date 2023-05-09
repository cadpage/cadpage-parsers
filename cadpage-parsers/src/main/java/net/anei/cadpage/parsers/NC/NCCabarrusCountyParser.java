package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCCabarrusCountyParser extends GroupBestParser {

  public NCCabarrusCountyParser() {
    super(new NCCabarrusCountyAParser(), new NCCabarrusCountyBParser());
  }

  static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "9075 US HWY 601",                      "+35.292348,-80.511689",
      "9075 US HWY 601 S",                    "+35.292348,-80.511689",
      "9690 US HWY 601 S",                    "+35.282832,-80.506780",
      "9701 US HWY 601 S",                    "+35.283938,-80.506790",
      "9825 US HWY 601 S",                    "+35.282971,-80.504064",
      "9901 US HWY 601 S",                    "+35.283063,-80.500264",
      "10640 US HWY 601 S",                   "+35.271192,-80.501760",
      "10730 US HWY 601 S",                   "+35.269542,-80.501997",
      "11350 US HWY 601 S",                   "+35.260784,-80.502224",
      "12125 US HWY 601 S",                   "+35.250585,-80.500878",
      "13280 US HWY 601 S",                   "+35.234622,-80.506740",
      "93250 US HWY 601 S",                   "+35.288985,-80.510182"
  });


  static Properties CITY_CODES = buildCodeTable(new String[]{
      "AMA",  "ALBEMARLE",
      "CHASE","CONCORD",          // ?????
      "CHL",  "CHARLOTTE",
      "CHG",  "CHINA GROVE",
      "CHGV", "CHINA GROVE",
      "CLT",  "CHARLOTTE",
      "CON",  "CONCORD",
      "COR",  "CORNELIUS",
      "CPD",  "CONCORD",
      "DAV",  "DAVIDSON",
      "GE",   "GEORGEVILLE",
      "GLD",  "GOLD HILL",
      "GOLD", "GOLD HILL",
      "HAR",  "HARRISBURG",
      "HUN",  "HUNTERSVILLE",
      "IND",  "INDIAN TRAIL",
      "KAN",  "KANNAPOLIS",
      "LAN",  "LANDIS",
      "LOC",  "LOCUST",
      "MEC",  "MECKLENBURG",
      "MID",  "MIDLAND",
      "MIN",  "MINT HILL",
      "MNR",  "MONROE",
      "MP",   "MT PLEASANT",
      "MTH",  "MOUNT HOLLY",
      "PNV",  "PINEVILLE",
      "ROC",  "ROCKWELL",
      "ROCK", "ROCKWELL",
      "SAL",  "SALISBURY",
      "STF",  "STANFIELD",
      "WAX",  "WAXHAW",

      "OOC",  "OOC"
  });

}
