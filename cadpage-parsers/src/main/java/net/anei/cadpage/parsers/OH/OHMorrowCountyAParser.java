package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

/**
 * Morrow County, OH
 */
public class OHMorrowCountyAParser extends DispatchA1Parser {

  public OHMorrowCountyAParser() {
    super("MORROW COUNTY", "OH"); 
  }
  
  @Override
  public String getFilter() {
    return "mc911@rrohio.com";
  }
}
