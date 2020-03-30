package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCAsheCountyBParser extends DispatchA71Parser {
  
  public NCAsheCountyBParser() {
    super("ASHE COUNTY", "NC");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
