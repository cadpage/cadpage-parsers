package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class NCMaconCountyCParser extends DispatchSPKParser {
  
  public NCMaconCountyCParser() {
    super("MACON COUNTY", "NC");
  }
  
  @Override
  public String getFilter() {
    return "cad@maconnc.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
