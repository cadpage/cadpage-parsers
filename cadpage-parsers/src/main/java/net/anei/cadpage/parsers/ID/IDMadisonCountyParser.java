package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class IDMadisonCountyParser extends DispatchA55Parser {

  public IDMadisonCountyParser() {
    super("MADISON COUNTY", "ID");
  }
  
  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }
}
