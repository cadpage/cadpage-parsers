package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALClayCountyParser extends DispatchA71Parser {

  public ALClayCountyParser() {
    super("CLAY COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "claye911@gmail.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
