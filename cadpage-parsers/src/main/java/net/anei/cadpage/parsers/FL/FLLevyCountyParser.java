package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

/**
 * Levy County, FL
 */

public class FLLevyCountyParser extends DispatchSPKParser {
  
  public FLLevyCountyParser() {
    super("LEVY COUNTY", "FL");
  }
  
  @Override
  public String getFilter() {
    return "levycad@levyso.com";
  }

}
