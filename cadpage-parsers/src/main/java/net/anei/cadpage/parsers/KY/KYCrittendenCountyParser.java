package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYCrittendenCountyParser extends DispatchA27Parser {

  public KYCrittendenCountyParser() {
    super("CRITTENDEN COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "marcritcoe911@cissystem.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
