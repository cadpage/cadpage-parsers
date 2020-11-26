package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class COClearCreekCountyAParser extends DispatchA55Parser {

  public COClearCreekCountyAParser() {
    super("CLEAR CREEK COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "cadalerts@messaging.eforcesoftware.net";
  }
}
