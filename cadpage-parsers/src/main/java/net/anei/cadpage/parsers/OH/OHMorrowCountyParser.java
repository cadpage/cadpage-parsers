package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

/**
 * Morrow County, OH
 */
public class OHMorrowCountyParser extends DispatchA1Parser {

  public OHMorrowCountyParser() {
    super("MORROW COUNTY", "OH"); 
  }
  
  @Override
  public String getFilter() {
    return "mc911@rrohio.com";
  }
}
