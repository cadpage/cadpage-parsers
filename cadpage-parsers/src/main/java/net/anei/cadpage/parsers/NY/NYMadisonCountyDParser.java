package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NYMadisonCountyDParser extends DispatchA19Parser {

  public NYMadisonCountyDParser() {
    super("MADISON COUNTY", "NY");
  }

  @Override
  public String getFilter() {
    return "e911@madisoncounty.ny.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
