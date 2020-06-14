package net.anei.cadpage.parsers.MT;

import net.anei.cadpage.parsers.dispatch.DispatchH04Parser;

public class MTFlatheadCountyCParser extends DispatchH04Parser {
  
  public MTFlatheadCountyCParser() {
    super("FLATHEAD COUNTY", "MT");
  }
  
  @Override
  public String getFilter() {
    return "911@flatheadoes.mt.gov,@flathead911.mt.gov>";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
