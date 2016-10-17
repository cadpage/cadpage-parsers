package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Hanover County, VA
 */
public class VAHanoverCountyParser extends GroupBestParser {
  
  public VAHanoverCountyParser() {
    super(new VAHanoverCountyAParser(), new VAHanoverCountyBParser());
  }
}
