package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;


public class NCNorthamptonCountyParser extends DispatchA3Parser {
  
  public NCNorthamptonCountyParser() {
    super(1, "Northampton911:*", "NORTHAMPTON COUNTY", "NC");
  }
  
  @Override
  public String getFilter() {
    return "Northampton911@nhcnc.net";
  }
}
