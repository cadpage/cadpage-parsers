package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.GroupBestParser;


public class CTTollandCountyParser extends GroupBestParser {
  
  public CTTollandCountyParser() {
    super(new CTTollandCountyAParser(), new CTTollandCountyBParser(), 
          new CTTollandCountyCParser(), new CTTollandCountyDParser());
  }
  
  static final String[] CITY_LIST = new String[]{
    "ANDOVER",
    "BOLTON",
    "COLUMBIA",
    "COVENTRY",
    "ELLINGTON",
    "HEBRON",
    "MANSFIELD",
    "SOMERS",
    "STAFFORD",
    "TOLLAND",
    "UNION",
    "VERNON",
    "WILLINGTON",

    "COVENTRY LAKE",
    "SOUTH COVENTRY",
    "CRYSTAL LAKE",
    "STAFFORD SPRINGS",
    "STORRS",
    "CENTRAL SOMERS",
    "ROCKVILLE",
    "MASHAPAUG",
    
    "WAREHOUSE POINT",

    "UCONN",
    
    // Fairfield County
    "NEWTOWN",
    
    // Hartford County
    "BROAD BROOK",
    "EAST WINDSOR",
    "ENFIELD",
    "GLASTONBURY",
    "HARTFORD",
    "MANCHESTER",
    "SOUTH WINDSOR",
    "WINDSOR LOCKS",
    "WINDSOR LOCKS EAST",
    
    // New London County
    "LEBANON",
    
    // Windham county
    "ASHFORD",
    "EASTFORD",
    "NORTH WINDHAM",
    "WILLIMANTIC",
    "WINDHAM",
    "WOODSTOCK"
  };
}
