package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class CODeloresCountyParser extends DispatchA64Parser {

  public CODeloresCountyParser() {
    super("DELORES COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "reports@messaging.eforcesoftware.net";
  }
}
