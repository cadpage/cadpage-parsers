package net.anei.cadpage.parsers.UT;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA66Parser;

public class UTSummitCountyParser extends DispatchA66Parser {
  
  public UTSummitCountyParser() {
    super(CITY_CODES, "SUMMIT COUNTY", "UT");
  }
  
  @Override
  public String getFilter() {
    return "scso911@summitcounty.org";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "COA",  "COALVILLE",
      "EVA",  "KAMAS",        // ?????
      "HEB",  "HEBER",        // Wasatch County
      "HOY",  "HOYTSVILLE",
      "KAM",  "KAMAS",
      "OAK",  "OAKLY",
      "PC",   "PARK CITY",
      "PCC",  "PARK CITY",
      "PEO",  "PEOA",
      "RKP",  "ROCKPORT"
  });
}
