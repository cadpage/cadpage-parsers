package net.anei.cadpage.parsers.RI;

import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;

public class RIWashingtonCountyBParser extends DispatchA32Parser {
  
  public RIWashingtonCountyBParser() {
    super(RIWashingtonCountyParser.CITY_LIST, "WASHINGTON COUNTY", "RI");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@charlestownrescue.org";
  }
}
