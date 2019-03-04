package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ALBlountCountyBParser extends DispatchSouthernParser {
  
  public ALBlountCountyBParser() {
    super(CITY_LIST, "BLOUNT COUNTY", "AL", 
         DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_X | DSFLG_NAME | DSFLG_PHONE | DSFLG_CODE | DSFLG_ID | DSFLG_TIME);
  }
  
  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.endsWith("MM")) return true;
    return super.isNotExtraApt(apt);
  }

  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "LEEDS",
    "ONEONTA",
    "WARRIOR",

    // Towns
    "ALLGOOD",
    "ALTOONA",
    "BLOUNTSVILLE",
    "CLEVELAND",
    "COUNTY LINE",
    "GARDEN CITY",
    "HAYDEN",
    "HIGHLAND LAKE",
    "LOCUST FORK",
    "NECTAR",
    "ROSA",
    "SNEAD",
    "SUSAN MOORE",
    "TRAFFORD",

    // Census-designated place
    "SMOKE RISE",

    // Unincorporated communities
    "BANGOR",
    "BLOUNT SPRINGS",
    "BRIGHT STAR",
    "BROOKSVILLE",
    "HOPEWELL",
    "LITTLE WARRIOR",
    "MOUNT HIGH",
    "NECTAR",
    "REMLAP",
    "SKY BALL",
    "STRAIGHT MOUNTAIN",
    "SUMMIT"
  };

}
