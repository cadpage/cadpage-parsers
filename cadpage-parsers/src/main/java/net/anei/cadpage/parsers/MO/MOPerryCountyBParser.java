package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOPerryCountyBParser extends DispatchBCParser {
  
  public MOPerryCountyBParser() {
    super("PERRY COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "PERRY@OMNIGO.COM";
  }

}
