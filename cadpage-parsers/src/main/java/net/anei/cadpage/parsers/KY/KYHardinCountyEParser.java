package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class KYHardinCountyEParser extends DispatchA19Parser {

  public KYHardinCountyEParser() {
    super("HARDIN COUNTY", "KY");
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
