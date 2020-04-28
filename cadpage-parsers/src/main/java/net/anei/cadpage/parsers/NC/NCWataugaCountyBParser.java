package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCWataugaCountyBParser extends DispatchA71Parser {
  
  public NCWataugaCountyBParser() {
    super("WATAUGA COUNTY", "NC");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
