package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Page County, VA
 */
public class VAPageCountyParser extends GroupBestParser {
  
  public VAPageCountyParser() {
    super(new VAPageCountyAParser(), new VAPageCountyBParser());
  }
}
