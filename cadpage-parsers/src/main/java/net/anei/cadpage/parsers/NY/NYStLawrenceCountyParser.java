package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class NYStLawrenceCountyParser extends DispatchA19Parser {
  
  public NYStLawrenceCountyParser() {
    super("ST LAWRENCE COUNTY", "NY");
  }
  
  @Override
  public String getFilter() {
    return "esspillmanalert@stlawco.org";
  }
}
	