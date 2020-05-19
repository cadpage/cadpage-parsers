package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCHendersonCountyBParser extends DispatchA71Parser {
  
  public NCHendersonCountyBParser() {
    super("HENDERSON COUNTY", "NC");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
