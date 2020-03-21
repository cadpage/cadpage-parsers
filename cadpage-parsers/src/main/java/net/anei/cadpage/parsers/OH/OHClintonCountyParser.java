package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA76Parser;

/**
 * Clinton County, OH
 */
public class OHClintonCountyParser extends DispatchA76Parser {

  public OHClintonCountyParser() {
    super("CLINTON COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "noreply@clintonsheriff.com";
  }
}
