package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYPerryCountyParser extends DispatchA27Parser {

  public KYPerryCountyParser() {
    super("PERRY COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "cadnotifications@perrycountyky.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
