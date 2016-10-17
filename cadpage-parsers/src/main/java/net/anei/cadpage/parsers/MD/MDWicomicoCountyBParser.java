package net.anei.cadpage.parsers.MD;


import net.anei.cadpage.parsers.dispatch.DispatchChiefPagingParser;

/**
 * Wicomico County, MD (B)
 */
public class MDWicomicoCountyBParser extends DispatchChiefPagingParser {
  
  public MDWicomicoCountyBParser() {
    super(CITY_LIST, "WICOMICO COUNTY", "MD");
  }
  
  private static String[] CITY_LIST = new String[]{
    
      // cities
      "FRUITLAND",
      "SALISBURY",
      
      // Towns
      "DELMAR",
      "HEBRON",
      "MARDELA SPRINGS",
      "PITTSVILLE",
      "SHARPTOWN",
      "WILLARDS",
      
      // Unincorporated
      "ALLEN",
      "BIVALVE",
      "NANTICOKE",
      "PARSONSBURG",
      "POWELLVILLE",
      "QUANTICO",
      "TYASKIN",
      "WHITEHAVEN"
  };
}


