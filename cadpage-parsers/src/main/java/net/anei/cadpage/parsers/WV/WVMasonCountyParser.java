package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;



public class WVMasonCountyParser extends DispatchB2Parser {
  
  public WVMasonCountyParser() {
    super("MASONCAD:", CITY_LIST, "MASON COUNTY", "WV");
  }
  
  @Override
  public String getFilter() {
    return "masoncad@masoncountyoes.com";
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Incorporated cities and towns
    "HARTFORD CITY",
    "HENDERSON",
    "LEON",
    "MASON",
    "NEW HAVEN",
    "POINT PLEASANT",

    // Unincorporated communities
    "AMBROSIA",
    "APPLE GROVE",
    "ARBUCKLE",
    "ARLEE",
    "ASHTON",
    "BADEN",
    "BEECH HILL",
    "CAPEHART",
    "CLIFTON",
    "COUCH",
    "ELMWOOD",
    "GALLIPOLIS FERRY",
    "GLENWOOD",
    "GREER",
    "GRIMMS LANDING",
    "HOGSETT",
    "LETART",
    "MERCERS BOTTOM",
    "NAT",
    "RAYBURN",
    "SASSAFRAS",
    "WEST COLUMBIA",
    "WYOMA",
    
    
    "CABELL COUNTY",
    "MILTON",
    
    // Jackson County
    "EVANS"

  };
}
