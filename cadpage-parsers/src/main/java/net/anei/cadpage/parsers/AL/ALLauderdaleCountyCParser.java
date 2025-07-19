package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class ALLauderdaleCountyCParser extends DispatchA19Parser {

  public ALLauderdaleCountyCParser() {
    super("LAUDERDALE COUNTY", "AL");
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
