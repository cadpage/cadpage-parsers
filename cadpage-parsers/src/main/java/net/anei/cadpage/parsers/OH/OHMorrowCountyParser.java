package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Morrow County, OH
 */

public class OHMorrowCountyParser extends GroupBestParser {
  
  public OHMorrowCountyParser() {
    super(new OHMorrowCountyAParser(), new OHMorrowCountyBParser());
  }
}
