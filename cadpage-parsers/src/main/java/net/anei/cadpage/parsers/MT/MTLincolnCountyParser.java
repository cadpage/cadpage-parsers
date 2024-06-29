package net.anei.cadpage.parsers.MT;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class MTLincolnCountyParser extends DispatchA19Parser {

  public MTLincolnCountyParser() {
    super("LINCOLN COUNTY", "MT");
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
