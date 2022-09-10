package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYTriggCountyBParser extends DispatchA27Parser {

  public KYTriggCountyBParser() {
    super("TRIGG COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org,trigg911@bellsouth.net,triggcounty911@gmail.com,triggco911@cissystem.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
