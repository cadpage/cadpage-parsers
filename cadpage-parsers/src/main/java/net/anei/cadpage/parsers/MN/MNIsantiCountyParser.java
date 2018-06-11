package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;

/**
 * Isanti County, MN
 */

public class MNIsantiCountyParser extends DispatchA43Parser {

  public MNIsantiCountyParser() {
    super(CITY_LIST, "ISANTI COUNTY", "MN");
  }
  
  private static final String[] CITY_LIST = new String[]{
  
      // Cities
      "BRAHAM",
      "CAMBRIDGE",
      "ISANTI",
  
      // Townships
      "ATHENS",
      "BRADFORD",
      "CAMBRIDGE",
      "DALBO",
      "ISANTI",
      "MAPLE RIDGE",
      "NORTH BRANCH",
      "OXFORD",
      "SPENCER BROOK",
      "SPRINGVALE",
      "STANCHFIELD",
      "STANFORD",
      "WYANETT",
  
      // Unincorporated communities
      "ANDREE",
      "ATHENS",
      "BLOMFORD",
      "BODUM",
      "BRADFORD",
      "CARMODY",
      "CROWN",
      "DALBO",
      "DAY",
      "EDGEWOOD",
      "ELM PARK",
      "GRANDY",
      "OXLIP",
      "PINE BROOK",
      "SPENCER BROOK",
      "SPRING LAKE",
      "SPRINGVALE",
      "STANLEY",
      "STANCHFIELD",
      "STANFORD",
      "WALBO",
      "WEBER",
      "WEST POINT",
      "WYANETT",
      
      // Mile Lacs County
      "PRINCETON",
      "SPENCER BROOK",
      
      // Not sure what this is, but we need to get rid of it
      "UPPER"
  };
}
