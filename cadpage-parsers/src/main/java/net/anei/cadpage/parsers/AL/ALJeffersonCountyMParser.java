package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class ALJeffersonCountyMParser extends DispatchA55Parser {

  public ALJeffersonCountyMParser() {
    super("JEFFERSON COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }
}
