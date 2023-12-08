package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYKnoxCountyBParser extends DispatchA27Parser {

  public KYKnoxCountyBParser() {
    super("KNOX COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org,knoxcoky911@cissystem.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
