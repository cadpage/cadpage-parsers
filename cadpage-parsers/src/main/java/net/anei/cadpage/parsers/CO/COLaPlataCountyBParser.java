package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class COLaPlataCountyBParser extends DispatchA19Parser {

  public COLaPlataCountyBParser() {
    super("LA PLATA COUNTY", "CO");
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
