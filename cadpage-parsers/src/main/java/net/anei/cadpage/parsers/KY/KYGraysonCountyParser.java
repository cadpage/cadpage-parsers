package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA93Parser;

public class KYGraysonCountyParser extends DispatchA93Parser {

  public KYGraysonCountyParser() {
    super("GRAYSON COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "CAD@gce911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
