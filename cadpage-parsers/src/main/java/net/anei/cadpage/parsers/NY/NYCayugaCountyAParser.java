package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NYCayugaCountyAParser extends DispatchA19Parser {

  public NYCayugaCountyAParser() {
    super("CAYUGA COUNTY", "NY");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com,911spillman@cayugacounty.us";
  }
}
