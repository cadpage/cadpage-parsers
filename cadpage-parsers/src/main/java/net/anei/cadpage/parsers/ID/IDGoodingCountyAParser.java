package net.anei.cadpage.parsers.ID;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA31Parser;


public class IDGoodingCountyAParser extends DispatchA31Parser {
  
  // Obsolete, replaced by IDGoodingCountyB as of 1/19/2015
  
  public IDGoodingCountyAParser() {
   this("GOODING COUNTY", "ID");
  }
  
  public IDGoodingCountyAParser(String defCity, String defState) {
    super("SIRCOMM", CITY_CODES, defCity, defState);
  }
  
  @Override
  public String getFilter() {
    return "PagingService@sircomm.com";
  }

  @Override
  public String getAliasCode() {
    return "IDGoodingACounty";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "HAZ AREA",  "HAZELTON",
      "HSN AREA",  "HANSEN",
      "KMB AREA",  "KIMBERLY",
      "MURT AREA", "MURTAUGH",
      "WND AREA",  "WENDELL"
  });
}
