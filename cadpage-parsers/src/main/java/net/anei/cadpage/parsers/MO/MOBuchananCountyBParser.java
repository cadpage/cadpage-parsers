package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA92Parser;

public class MOBuchananCountyBParser extends DispatchA92Parser {
  public MOBuchananCountyBParser() {
    super("BUCHANAN COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "sa@logis.dk";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
