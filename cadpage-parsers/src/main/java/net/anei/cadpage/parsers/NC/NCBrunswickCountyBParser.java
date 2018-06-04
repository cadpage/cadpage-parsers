package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCBrunswickCountyBParser extends DispatchA71Parser {
  
  public NCBrunswickCountyBParser() {
    super("BRUNSWICK COUNTY", "NC");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
