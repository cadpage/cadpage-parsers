package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class COClearCreekCountyAParser extends DispatchA55Parser {

  public COClearCreekCountyAParser() {
    super("CLEAR CREEK COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "reports@messaging.eforcesoftware.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
