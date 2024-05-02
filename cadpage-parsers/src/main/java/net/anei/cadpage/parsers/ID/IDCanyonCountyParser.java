package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class IDCanyonCountyParser extends DispatchA19Parser {

  public IDCanyonCountyParser() {
    super("CANYON COUNTY", "ID");
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
