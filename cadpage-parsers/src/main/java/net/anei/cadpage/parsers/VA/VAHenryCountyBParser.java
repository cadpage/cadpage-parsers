package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class VAHenryCountyBParser extends DispatchA71Parser {
  
  public VAHenryCountyBParser() {
    super("HENRY COUNTY", "VA");
  }
  
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
