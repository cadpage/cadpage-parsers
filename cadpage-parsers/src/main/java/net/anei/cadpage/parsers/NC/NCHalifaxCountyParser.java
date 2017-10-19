package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;


public class NCHalifaxCountyParser extends DispatchA3Parser {
  
  public NCHalifaxCountyParser() {
    super(0, "HalifaxCoE911:*", "HALIFAX COUNTY", "NC");
  }
  
  @Override
  public String getFilter() {
    return "HalifaxCoE911@HalifaxNC911.com,HalifaxCoE911@HalifaxNC.com";
  }
}
