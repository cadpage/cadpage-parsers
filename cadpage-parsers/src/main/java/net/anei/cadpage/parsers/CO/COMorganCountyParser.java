package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Morgan County, CO
 */

public class COMorganCountyParser extends DispatchA27Parser {
  
  public COMorganCountyParser() {
    super("MORGAN COUNTY", "CO", "[A-Z]+\\d+|\\d{8}");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
