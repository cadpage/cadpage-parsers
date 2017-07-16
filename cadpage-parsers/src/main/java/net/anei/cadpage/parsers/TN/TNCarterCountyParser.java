package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;


public class TNCarterCountyParser extends DispatchA65Parser {
  
  public TNCarterCountyParser() {
    super(CITY_LIST, "CARTER COUNTY", "TN");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@911email.net,dispatch@911comm1.info";
  } 
  
  private static final String[] CITY_LIST = new String[]{
      
      // Cities
      "ELIZABETHTON",
      "JOHNSON CITY",
      "WATAUGA",

      // Census-designated places
      "CENTRAL",
      "HUNTER",
      "PINE CREST",
      "ROAN MOUNTAIN",

      // Unincorporated communities
      "BIG SPRING",
      "BITTER END",
      "BUTLER",
      "FISH SPRINGS",
      "HAMPTON",
      "MILLIGAN COLLEGE",
      "STONEY CREEK",
      "TIGER VALLEY",
      "VALLEY FORGE"
  };
}
