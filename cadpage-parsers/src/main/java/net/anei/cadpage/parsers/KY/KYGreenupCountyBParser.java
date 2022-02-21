package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYGreenupCountyBParser extends DispatchA27Parser {

  public KYGreenupCountyBParser() {
    super("GREENUP COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "ciscad@greenupe911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
