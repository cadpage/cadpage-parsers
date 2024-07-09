package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA77Parser;

public class OHSanduskyCountyParser extends DispatchA77Parser {

  public OHSanduskyCountyParser() {
    super("Fire Call", "SANDUSKY COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
