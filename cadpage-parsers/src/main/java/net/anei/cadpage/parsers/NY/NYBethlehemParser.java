package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.dispatch.DispatchA5Parser;

/**
 * Bethlehem, NY
 */
public class NYBethlehemParser extends DispatchA5Parser {
    
  public NYBethlehemParser() {
    super("BETHLEHEM", "NY");
  }
  
  @Override
  public String getFilter() {
    return "@TOWNOFBETHLEHEM.ORG";
  }
}
