package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class GATiftCountyBParser extends DispatchA19Parser {
  
  public GATiftCountyBParser() {
    super("TIFT COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "@alert.active911.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
