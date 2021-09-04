package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NCWayneCountyDParser extends DispatchA19Parser {

  public NCWayneCountyDParser() {
    super("WAYNE COUNTY", "NC");
  }

  @Override
  public String getFilter() {
    return "@alert.active911.com,WayneCounty911@waynegov.com";
  }
}
