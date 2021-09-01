package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class COAdamsCountyCParser extends DispatchA19Parser {

  public COAdamsCountyCParser() {
    super("ADAMS COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "noreply@fedheights.org,@alert.active911.com";
  }
}
