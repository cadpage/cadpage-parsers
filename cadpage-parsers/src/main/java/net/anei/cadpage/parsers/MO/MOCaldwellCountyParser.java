package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOCaldwellCountyParser extends DispatchBCParser {
  
  public MOCaldwellCountyParser() {
    super("CALDWELL COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "CALDWELL003@CALDWELLCO.MISSOURI.ORG";
  }
}
