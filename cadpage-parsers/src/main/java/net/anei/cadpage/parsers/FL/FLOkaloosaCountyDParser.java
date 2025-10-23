package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class FLOkaloosaCountyDParser extends DispatchA19Parser {

  public FLOkaloosaCountyDParser() {
    super("OKALOOSA COUNTY", "FL");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

}
