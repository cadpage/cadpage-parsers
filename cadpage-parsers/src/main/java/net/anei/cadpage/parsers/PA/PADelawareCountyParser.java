package net.anei.cadpage.parsers.PA;

import java.util.Properties;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Delaware County, PA

*/

public class PADelawareCountyParser extends GroupBestParser {

  public PADelawareCountyParser() {
    super(new PADelawareCountyAParser(),
          new PADelawareCountyBParser(),
          new PADelawareCountyCParser(),
          new PADelawareCountyDParser(),
          new PADelawareCountyEParser(),
          new PADelawareCountyFParser(),
          new PADelawareCountyGParser(),
          new PADelawareCountyHParser());
  }

  static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AL", "ALDAN",
      "AS", "ASTON TWP",
      "BE", "BETHEL TWP",
      "BM", "BIRMINGHAM TWP",
      "BR", "BROOKHAVEN",
      "CC", "CHESTER",
      "CF", "CHADDS FORD TWP",
      "CH", "CHESTER HEIGHTS",
      "CL", "CLIFTON HEIGHTS",
      "CL DE", "CLAYMONT/DE",
      "CN", "CONCORD TWP",
      "CO", "COLLINGDALE",
      "CW", "COLWYN",
      "DB", "DARBY",
      "DE", "NEW CASTLE COUNTY/DE",
      "DT", "DARBY TWP",
      "ED", "EDDYSTONE",
      "EG", "EDGEMONT TWP",
      "EL", "EAST LANSDOWNE",
      "ES TN", "ESSINGTON", // SECTION OF TINICUM TWP
      "FL", "FOLCROFT",
      "GL", "GLENOLDEN",
      "HV", "HAVERFORD TWP",
      "LA", "LANSDOWNE",
      "LC", "LOWER CHICHESTER TWP",
      "LM", "LOWER MERION TWP",  // In montgomery conty
      "LS TN", "LESTER", // SECTION OF TINICUM TWP
      "MB", "MILLBOURNE",
      "MD", "MIDDLETOWN TWP",
      "ME", "MEDIA",
      "MH", "MARCUS HOOK",
      "MO", "MORTON",
      "MP", "MARPLE TWP",
      "NC", "NEW CASTLE COUNTY",
      "NCC","NEW CASTLE COUNTY",
      "NP", "NETHER PROVIDENCE TWP",
      "NT", "NEWTOWN TWP",
      "NW", "NORWOOD",
      "PK", "PARKSIDE",
      "PP", "PROSPECT PARK",
      "RN", "RADNOR TWP",
      "RP", "RIDLEY PARK",
      "RT", "RIDLEY TWP",
      "RU", "RUTLEDGE",
      "RV", "ROSE VALLEY",
      "SH", "SHARON HILL",
      "SP", "SPRINGFIELD TWP",
      "SW", "SWARTHMORE",
      "TB", "THORNBURY TWP",
      "TC", "CHESTER TWP",
      "TD", "TREDYFFRIN TWP",
      "TN", "TINICUM TWP",
      "TR", "TRAINER",
      "UC", "UPPER CHICHESTER TWP",
      "UD", "UPPER DARBY TWP",
      "UL", "UPLAND",
      "UP", "UPPER PROVIDENCE TWP",
      "WT", "WILLISTOWN TWP",
      "YE", "YEADON",

      "DE", "NEW CASTLE COUNTY/DE",

      "MONTCO",       "MONTGOMERY COUNTY",
      "UMT MONTCO",   "UPPER MERION TWP",

      "NCC",          "NEW CASTLE COUNTY/DE",
      "NC",           "NEW CASTLE COUNTY/DE",
      "NEW CASTLE",   "NEW CASTLE COUNTY/DE",

      "LOWER MERION",      "LOWER MERION TWP",
      "LOWER MERION TWP",  "LOWER MERION TWP",
      "UPPER MERION",      "UPPER MERION TWP",
      "UPPER MERION TWP",  "UPPER MERION TWP"

  });
}
