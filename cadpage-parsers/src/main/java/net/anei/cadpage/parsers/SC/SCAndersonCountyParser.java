package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCAndersonCountyParser extends GroupBestParser {
  
  public SCAndersonCountyParser() {
    super(new SCAndersonCountyAParser(), new SCAndersonCountyBParser(), new SCAndersonCountyCParser());
  }

  static final String[] CITY_LIST = new String[]{
    "ANDERSON", 
    "BELTON", 
    "CLEMSON", 
    "EASLEY",
    "HONEA PATH",
    "IVA",
    "PELZER", 
    "STARR",
    "WEST PELZER",
    "WILLIAMSTON",
    "CENTERVILLE",
    "HOMELAND PARK",
    "LA FRANC",
    "NORTHLAKE",
    "PIEDMONT", 
    "POWDERSVILLE",
    "SANDY SPRINGS",
    "TOWNVILLE",
    
    "PENDLETON",
    
    // Greenville County
    "GREENVILLE",
    
    // Oconee County
    "FAIR PLAY"
  };

}
