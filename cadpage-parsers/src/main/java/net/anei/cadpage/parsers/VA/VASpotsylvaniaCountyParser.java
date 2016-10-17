package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchA61Parser;

public class VASpotsylvaniaCountyParser extends DispatchA61Parser {
 
  public VASpotsylvaniaCountyParser() {
    super("Mail from Archonix \n\n", "SPOTSYLVANIA COUNTY", "VA");
  }
  
  public String getFilter() {
    return "firedispatch@spotsylvania.va.us";
  }
}