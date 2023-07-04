package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class ORJeffersonCountyCParser extends DispatchA19Parser {

  public ORJeffersonCountyCParser() {
    super("JEFFERSON COUNTY", "OR");
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
