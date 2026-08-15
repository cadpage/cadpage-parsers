package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WVWayneCountyParser extends DispatchA19Parser {

  public WVWayneCountyParser() {
    super("WAYNE COUNTY", "WV");
  }

  @Override
  public String getFilter()  {
    return "FRN-wayne911@email.getrave.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
