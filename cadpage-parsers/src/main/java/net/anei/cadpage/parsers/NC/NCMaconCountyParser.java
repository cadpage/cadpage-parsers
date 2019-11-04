package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCMaconCountyParser extends GroupBestParser {
  
  public NCMaconCountyParser() {
    super(new NCMaconCountyAParser(), 
          new NCMaconCountyBParser(),
          new NCMaconCountyCParser(),
          new NCMaconCountyDParser());
  }

  static final String[] CITY_LIST = new String[]{
    "FRANKLIN", 
    "HIGHLANDS", 
    "OTTO",
    "FRANKLIN TWP", 
    "HIGHLANDS TWP", 
    "SUGARFORK TWP", 
    "BURNINGTOWN TWP",
    "CARTOOGECHAYE TWP", 
    "ELLIJAY TWP", 
    "MILLSHOAL TWP",
    "NANTAHALA TWP", 
    "SMITHBRIDGE TWP",
    
    // Cherokee County
    "TOPTON"
  };
}
