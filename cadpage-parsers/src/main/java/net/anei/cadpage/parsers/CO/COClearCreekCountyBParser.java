package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class COClearCreekCountyBParser extends DispatchA64Parser {

  public COClearCreekCountyBParser() {
    super("CLEAR CREEK COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "cadalerts@messaging.eforcesoftware.net";
  }
}
