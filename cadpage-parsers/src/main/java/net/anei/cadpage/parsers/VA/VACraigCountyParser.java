package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class VACraigCountyParser extends DispatchA71Parser {
  
  public VACraigCountyParser() {
    super("CRAIG COUNTY", "VA");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
