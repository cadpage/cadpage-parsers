package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Fairfax County, VA
 */
public class VAFairfaxCountyParser extends GroupBestParser {
  
  public VAFairfaxCountyParser() {
    super(new VAFairfaxCountyAParser(), new VAFairfaxCountyBParser());
  }
}
