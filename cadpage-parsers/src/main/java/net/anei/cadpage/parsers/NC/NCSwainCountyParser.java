package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCSwainCountyParser extends DispatchA71Parser {
  
  public NCSwainCountyParser() {
    super("SWAIN COUNTY", "NC");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
