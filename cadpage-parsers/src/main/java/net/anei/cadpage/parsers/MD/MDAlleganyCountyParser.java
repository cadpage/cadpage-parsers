package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class MDAlleganyCountyParser extends DispatchA19Parser {

  public MDAlleganyCountyParser() {
    super("ALLEGANY COUNTY", "MD");
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
