package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WAWhitmanCountyBParser extends DispatchA19Parser {

  public WAWhitmanCountyBParser() {
    super("WHITMAN COUNTY", "WA");
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
