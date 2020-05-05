package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALFranklinCountyParser extends DispatchA71Parser {
  
  public ALFranklinCountyParser() {
    super("FRANKLIN COUNTY", "AL");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
