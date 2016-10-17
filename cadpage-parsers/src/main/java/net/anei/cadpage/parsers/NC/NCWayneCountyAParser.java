package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA15Parser;


public class NCWayneCountyAParser extends DispatchA15Parser {
  
  public NCWayneCountyAParser() {
    super("WAYNE COUNTY", "NC");
  }
  
  @Override
  public String getFilter() {
    return "Notification@usamobility.net";
  }
}
