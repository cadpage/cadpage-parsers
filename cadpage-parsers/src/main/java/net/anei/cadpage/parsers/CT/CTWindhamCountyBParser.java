package net.anei.cadpage.parsers.CT;


public class CTWindhamCountyBParser extends CTNewHavenCountyBParser {
  
  public CTWindhamCountyBParser() {
    super(CITY_LIST, "WINDHAM COUNTY", "CT");
    setupSpecialStreets("NORTH WINDHAM LINE");
  }
  
  @Override
  public String getFilter() {
    return "wpdpaging@gmail.com";
  }
  
  private static final String[] CITY_LIST = new String[]{
    "ASHFORD",
    "BROOKLYN",
        "EAST BROOKLYN",
    "CANTERBURY",
    "CHAPLIN",
    "EASTFORD",
    "HAMPTON",
    "KILLINGLY",
        "DANIELSON",
    "PLAINFIELD",
        "CENTRAL VILLAGE",
        "MOOSUP",
        "PLAINFIELD VILLAGE",
        "WAUREGAN",
    "POMFRET",
    "PUTNAM",
        "PUTNAM DISTRICT",
    "SCOTLAND",
    "STERLING",
        "ONECO",
    "THOMPSON",
        "NORTH GROSVENOR DALE",
        "QUINEBAUG",
    "WINDHAM",
        "NORTH WINDHAM",
        "SOUTH WINDHAM",
        "WILLIMANTIC",
        "WINDHAM CENTER",
    "WOODSTOCK",
        "SOUTH WOODSTOCK",
        
    // New London County
    "FRANKLIN",
    "LEBANON",
    
    // Tolland County
    "ANDOVER",
    "COLUMBIA",
    "COVENTRY",
    "HEBRON",
    "MANSFIELD",
    "STORRS",
    "WILLINGTON"
  };
}
