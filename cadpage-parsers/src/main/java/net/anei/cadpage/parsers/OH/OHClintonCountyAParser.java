package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA76Parser;

/**
 * Clinton County, OH
 */
public class OHClintonCountyAParser extends DispatchA76Parser {

  public OHClintonCountyAParser() {
    super("CLINTON COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "noreply@clintonsheriff.com";
  }
}
