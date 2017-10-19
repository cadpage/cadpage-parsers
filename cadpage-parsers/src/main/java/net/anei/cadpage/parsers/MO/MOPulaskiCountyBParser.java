package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


/**
 * Pulaski County, MO
 */
public class MOPulaskiCountyBParser extends DispatchA19Parser {
  
  public MOPulaskiCountyBParser() {
    super("PULASKI COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH";
  }
}
