package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Jasper County, IN
 */

public class INJasperCountyParser extends DispatchA27Parser {
  
  public INJasperCountyParser() {
    super("JASPER COUNTY", "IN", "[A-Z]+\\d+|[A-Z]{1,3}FD(?:TEST)?");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
