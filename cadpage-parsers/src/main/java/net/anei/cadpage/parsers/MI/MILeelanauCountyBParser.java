package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchA77Parser;

public class MILeelanauCountyBParser extends DispatchA77Parser {
  
  public MILeelanauCountyBParser() {
    super("Rapid Notifications", MILeelanauCountyParser.CITY_CODES, "LEELANAU COUNTY", "MI");
  }
  
  @Override
  public String getFilter() {
    return "outgoing@co.leelanau.mi.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
