package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class OHHuronCountyParser extends DispatchA1Parser {
  
  public OHHuronCountyParser() {
    super("HURON COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "@alertsmtp.huroncountyema.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
