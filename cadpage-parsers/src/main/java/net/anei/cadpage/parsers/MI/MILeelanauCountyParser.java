package net.anei.cadpage.parsers.MI;

import java.util.Properties;

import net.anei.cadpage.parsers.GroupBestParser;

public class MILeelanauCountyParser extends GroupBestParser {
  
  public MILeelanauCountyParser() {
    super(new MILeelanauCountyAParser(), new MILeelanauCountyBParser());
  }
  
  static final Properties CITY_CODES = buildCodeTable(new String[]{
    "001", "CEDAR",
    "002", "EMPIRE",
    "003", "GLEN ARBOR",
    "004", "LAKE LEELANAU",
    "005", "LELAND",
    "006", "MAPLE CITY",
    "007", "NORTHPORT",
    "008", "OMENA",
    "009", "SUTTONS BAY",
    "010", "TRAVERSE CITY"
  });
}
