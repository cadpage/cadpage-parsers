package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

public class CAKingsCountyParser extends DispatchA20Parser {
  
  public CAKingsCountyParser() {
    super("KINGS COUNTY", "CA");
  }
  
  @Override
  public String getFilter() {
    return "@cityofhanfordca.com";
  }
}
