package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class MSHarrisonCountyDParser extends DispatchA19Parser {

  public MSHarrisonCountyDParser() {
    super("HARRISON COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

}
