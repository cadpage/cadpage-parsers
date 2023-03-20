package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class ALJeffersonCountyMParser extends DispatchA64Parser {
  
  public ALJeffersonCountyMParser() {
    super("JEFFERSON COUNTY", "AL");
  }
  
  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }
}
