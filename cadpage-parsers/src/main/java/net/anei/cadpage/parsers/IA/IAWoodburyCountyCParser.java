package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class IAWoodburyCountyCParser extends DispatchA19Parser {

  public IAWoodburyCountyCParser() {
    super("WOODBURY COUNTY", "IA");
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
