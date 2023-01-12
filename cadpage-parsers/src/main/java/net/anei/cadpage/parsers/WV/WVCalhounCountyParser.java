package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class WVCalhounCountyParser extends DispatchA74Parser {

  public WVCalhounCountyParser() {
    super("CALHOUN COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "Dispatch@calhounwv911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
