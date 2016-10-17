package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchA11Parser;

/**
 * Sussex County, NJ
 */
public class NJSussexCountyBParser extends DispatchA11Parser {
  
  
  public NJSussexCountyBParser() {
    super("SUSSEX COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "paging@sussexcountysheriff.com";
  }
}
