package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class TNRheaCountyBParser extends DispatchA65Parser {
  
  public TNRheaCountyBParser() {
    super(CITY_LIST, "RHEA COUNTY", "TN");
  }
  
  @Override
  public String getFilter() {
    return "rheacotn@911email.net,dispatch@911email.org";
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "DAYTON",

    // Towns
    "GRAYSVILLE",
    "SPRING CITY",

    // Unincorporated communities
    "EVENSVILLE",
    "FIVE POINTS",
    "FRAZIER",
    "GRANDVIEW",
    "LIBERTY HILL",
    "OGDEN",
    "OLD WASHINGTON",

    // Former community
    "RHEA SPRINGS"
  };

}
