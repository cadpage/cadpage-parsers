package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Boone County, IL
 */

public class ILCookCountyJParser extends DispatchA27Parser {
  
  public ILCookCountyJParser() {
    super("COOK COUNTY", "IL");
  }
  
  @Override
  public String getFilter() {
    return "noreply@everbridge.net";
  }
}
