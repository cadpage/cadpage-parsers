package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;


/**
 * Grand Traverse County, MI
 */
public class MIGrandTraverseCountyParser extends DispatchA9Parser {
  
  public MIGrandTraverseCountyParser() {
    super(null, "GRAND TRAVERSE COUNTY", "MI");
  }
  
  @Override
  public String getFilter() {
    return "itsmtp@grandtraverse.org";
  }
  
  
}
