package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOClayCountyDParser extends DispatchBCParser {
  
  public MOClayCountyDParser() {
    super("CLAY COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "NOREPLY@OMNIGO.CO";
  }

}
