package net.anei.cadpage.parsers.DE;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;

/**
 * Kent County, DE
 */
public class DEKentCountyDParser extends DispatchA9Parser {
  
  public DEKentCountyDParser() {
    super("KENT COUNTY", "DE");
  }
  
  @Override
  public String getFilter() {
    return "kentcenter@state.de.us";
  }
}


