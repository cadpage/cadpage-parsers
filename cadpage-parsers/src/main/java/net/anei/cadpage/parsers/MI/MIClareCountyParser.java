package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

/**
 * Clare County, OR
 */
public class MIClareCountyParser extends DispatchA20Parser {
  
  public MIClareCountyParser() {
    super("CLARE COUNTY", "MI");
  }
  
  @Override
  public String getFilter() {
    return "@clareco.net";
  }
 
}
