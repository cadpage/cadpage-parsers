package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;

/**
 * Mendocino County, CA (B)
 */
public class CAMendocinoCountyBParser extends DispatchA9Parser {
  
  public CAMendocinoCountyBParser() {
    super("MENDOCINO COUNTY", "CA");
  }
  
  @Override
  public String getFilter() {
    return "gambleb@co.mendocino.ca.us";
  }
}


