package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class CALosAngelesCountyGParser extends DispatchA24Parser {

  public CALosAngelesCountyGParser() {
    super("LOS ANGELES COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "paging@10-8systems.com";
  }
}
